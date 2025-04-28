import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("eclipse")
    id("idea")
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("org.spongepowered.mixin") version "0.7.+"
}

val modVersion: String by extra
val forgeVersion: String by extra
val parchmentMappingsVersion: String by extra
val minecraftVersion: String by extra
val forgeVersionRange: String by extra
val loaderVersionRange: String by extra
val mekanismVersion: String by extra
val mekanismVersionRange: String by extra
val curiosVersion: String by extra
val curiosVersionRange: String by extra

version = "$minecraftVersion-$modVersion"
group = "dev.tonimatas.mekanismcurios"

base {
    archivesName = "mekanismcurios"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

minecraft {
    mappings("parchment", "$parchmentMappingsVersion-$minecraftVersion")

    copyIdeResources = true

    runs {
        create("client") {
            property("forge.enabledGameTestNamespaces", "mekanismcurios")
        }

        create("server") {
            property("forge.enabledGameTestNamespaces", "mekanismcurios")
            args("--nogui")
        }
        
        create("data") {
            workingDirectory(project.file("run-data"))
            args("--mod", "mekanismcurios", "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }

        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            mods {
                create("mekanismcurios") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

mixin {
    add(sourceSets.main.get(), "mekanismcurios.refmap.json")
    config("mekanismcurios.mixins.json")
}

repositories {
    maven("https://modmaven.dev/")
    maven("https://maven.theillusivec4.top/")
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraftVersion}-${forgeVersion}")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
    compileOnly(fg.deobf("mekanism:Mekanism:$minecraftVersion-$mekanismVersion:api"))
    compileOnly(fg.deobf("mekanism:Mekanism:$minecraftVersion-$mekanismVersion"))
    compileOnly(fg.deobf("top.theillusivec4.curios:curios-forge:$minecraftVersion-$curiosVersion:api"))
    runtimeOnly(fg.deobf("mekanism:Mekanism:$minecraftVersion-$mekanismVersion"))
    runtimeOnly(fg.deobf("top.theillusivec4.curios:curios-forge:$minecraftVersion-$curiosVersion"))
}

tasks.processResources {
    var replaceProperties = mapOf("modVersion" to modVersion, "loaderVersionRange" to loaderVersionRange,
        "forgeVersionRange" to forgeVersionRange, "minecraftVersion" to minecraftVersion,
        "mekanismVersionRange" to mekanismVersionRange, "curiosVersionRange" to curiosVersionRange)

    inputs.properties(replaceProperties)
    filesMatching("META-INF/mods.toml") {
        expand(replaceProperties)
    }
}

tasks.named<Jar>("jar") {
    manifest {
        attributes(
            mapOf(
                "Specification-Title"     to "mekanismcurios", 
                "Specification-Vendor"    to "TonimatasDEV", 
                "Specification-Version"   to "1", 
                "Implementation-Title"    to "Mekanism Curios", 
                "Implementation-Version"  to project.tasks.jar.get().archiveVersion.get(), 
                "Implementation-Vendor" to "TonimatasDEV", 
                "Implementation-Timestamp" to ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")),
                "MixinConfigs" to "mekanismcurios.mixins.json"
            )
        )
    }

    finalizedBy("reobfJar")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
