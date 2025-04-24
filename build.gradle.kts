plugins {
    id("idea")
    id("java-library")
    id("net.neoforged.moddev") version "2.0.78"
}

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

val modVersion: String by extra
val neoVersion: String by extra
val parchmentMappingsVersion: String by extra
val parchmentMinecraftVersion: String by extra
val minecraftVersion: String by extra
val neoVersionRange: String by extra
val loaderVersionRange: String by extra
val mekanismVersion: String by extra
val mekanismVersionRange: String by extra
val curiosVersion: String by extra
val curiosVersionRange: String by extra

version = modVersion
group = "dev.tonimatas.mekanismcurios"

base {
    archivesName = "mekanismcurios"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = neoVersion

    parchment {
        mappingsVersion = parchmentMappingsVersion
        minecraftVersion = parchmentMinecraftVersion
    }

    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", "mekanismcurios")
        }

        create("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", "mekanismcurios")
        }
        
        create("data") {
            data()
            programArguments.addAll("--mod", "mekanismcurios", "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create("mekanismcurios") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

val localRuntime: Configuration by configurations.creating

configurations["runtimeClasspath"].extendsFrom(localRuntime)

repositories {
    maven("https://modmaven.dev/")
    maven("https://maven.theillusivec4.top/")
}

dependencies {
    compileOnly("mekanism:Mekanism:$minecraftVersion-$mekanismVersion:api")
    compileOnly("mekanism:Mekanism:$minecraftVersion-$mekanismVersion")
    compileOnly("top.theillusivec4.curios:curios-neoforge:$curiosVersion+$minecraftVersion:api")
    localRuntime("mekanism:Mekanism:$minecraftVersion-$mekanismVersion")
    runtimeOnly("top.theillusivec4.curios:curios-neoforge:$curiosVersion+$minecraftVersion")
}

tasks.processResources {
    var replaceProperties = mapOf("modVersion" to modVersion, "loaderVersionRange" to loaderVersionRange,
        "neoVersionRange" to neoVersionRange, "minecraftVersion" to minecraftVersion,
        "mekanismVersionRange" to mekanismVersionRange, "curiosVersionRange" to curiosVersionRange)

    inputs.properties(replaceProperties)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
