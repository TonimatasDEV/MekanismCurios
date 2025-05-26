package dev.tonimatas.mekanismcurios.networking;

import dev.tonimatas.mekanismcurios.MekanismCurios;
import dev.tonimatas.mekanismcurios.util.CuriosSlots;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.event.MekanismTeleportEvent;
import mekanism.common.attachments.FrequencyAware;
import mekanism.common.content.teleporter.TeleporterFrequency;
import mekanism.common.item.ItemPortableTeleporter;
import mekanism.common.lib.frequency.Frequency;
import mekanism.common.lib.frequency.FrequencyType;
import mekanism.common.network.PacketUtils;
import mekanism.common.network.to_client.PacketPortalFX;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.tile.TileEntityTeleporter;
import mekanism.common.util.StorageUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record QuickTeleportActionPacket() implements CustomPacketPayload {
    public static final Type<QuickTeleportActionPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(MekanismCurios.MODID, "quick_teleport_action_packet"));

    public static final StreamCodec<FriendlyByteBuf, QuickTeleportActionPacket> STREAM_CODEC = StreamCodec.unit(new QuickTeleportActionPacket());
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // Modified method from PacketPortableTeleporterTeleport#handle
    public static void handle(final QuickTeleportActionPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();

            ItemStack stack = CuriosSlots.TELEPORTER.getItemStack(player);

            if (!stack.isEmpty() && stack.getItem() instanceof ItemPortableTeleporter item) {
                Frequency.FrequencyIdentity identity = getIdentity(item, stack);
                
                if (identity == null) {
                    return;
                }
                
                TeleporterFrequency found = FrequencyType.TELEPORTER.getFrequency(identity, player.getUUID());
                if (found == null) {
                    return;
                }
                GlobalPos coords = found.getClosestCoords(GlobalPos.of(player.level().dimension(), player.blockPosition()));
                if (coords != null) {
                    MinecraftServer server = player.level().getServer();
                    Level teleWorld = server == null ? null : server.getLevel(coords.dimension());
                    TileEntityTeleporter teleporter = WorldUtils.getTileEntity(TileEntityTeleporter.class, teleWorld, coords.pos());
                    if (teleporter != null) {
                        long energyCost;
                        Runnable energyExtraction = null;
                        if (!player.isCreative()) {
                            energyCost = TileEntityTeleporter.calculateEnergyCost(player, teleWorld, coords);
                            IEnergyContainer energyContainer = StorageUtils.getEnergyContainer(stack, 0);
                            if (energyContainer == null || energyContainer.extract(energyCost, Action.SIMULATE, AutomationType.MANUAL) < energyCost) {
                                return;
                            }
                            energyExtraction = () -> energyContainer.extract(energyCost, Action.EXECUTE, AutomationType.MANUAL);
                        } else {
                            energyCost = 0L;
                        }
                        //TODO: Figure out what this try catch is meant to be catching as I don't see much of a reason for it to exist
                        try {
                            teleporter.didTeleport.add(player.getUUID());
                            teleporter.teleDelay = 5;
                            BlockPos teleporterTargetPos = teleporter.getTeleporterTargetPos();
                            MekanismTeleportEvent.PortableTeleporter event = new MekanismTeleportEvent.PortableTeleporter(player, teleporterTargetPos, coords.dimension(), stack, energyCost);
                            if (NeoForge.EVENT_BUS.post(event).isCanceled()) {
                                //Fail if the event was cancelled
                                return;
                            }
                            if (energyExtraction != null) {
                                energyExtraction.run();
                            }
                            player.connection.aboveGroundTickCount = 0;
                            player.closeContainer();
                            PacketUtils.sendToAllTracking(new PacketPortalFX(player.blockPosition()), player.level(), coords.pos());
                            if (player.isPassenger()) {
                                player.stopRiding();
                            }
                            double oldX = player.getX();
                            double oldY = player.getY();
                            double oldZ = player.getZ();
                            Level oldWorld = player.level();
                            TileEntityTeleporter.teleportEntityTo(player, teleWorld, teleporter, event, false, DimensionTransition.DO_NOTHING);
                            if (player.level() != oldWorld || player.distanceToSqr(oldX, oldY, oldZ) >= 25) {
                                //If the player teleported over 5 blocks, play the sound at both the destination and the source
                                oldWorld.playSound(null, oldX, oldY, oldZ, SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                            }
                            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS);
                            teleporter.sendTeleportParticles();
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        });
    }
    
    private static Frequency.FrequencyIdentity getIdentity(ItemPortableTeleporter teleporter, ItemStack stack) {
        DataComponentType<? extends FrequencyAware<?>> frequencyComponent = MekanismDataComponents.getFrequencyComponent(teleporter.getFrequencyType());
        if (frequencyComponent == null) {
            return null;
        }
        FrequencyAware<?> frequencyAware = stack.get(frequencyComponent);
        if (frequencyAware != null) {
            Frequency.FrequencyIdentity identity = frequencyAware.identity().orElse(null);
            if (identity != null) {
                return identity;
            }
        }
        
        return null;
    }
}
