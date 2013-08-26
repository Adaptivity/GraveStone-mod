package GraveStone;

import GraveStone.entity.EntitySkeletonCat;
import GraveStone.entity.EntitySkeletonDog;
import GraveStone.entity.EntityZombieCat;
import GraveStone.entity.EntityZombieDog;
import cpw.mods.fml.common.registry.EntityRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class GraveStoneEntity {

    private static GraveStoneEntity instance;

    private GraveStoneEntity() {
        getEntity();
    }

    public static GraveStoneEntity getInstance() {
        if (instance == null) {
            return new GraveStoneEntity();
        } else {
            return instance;
        }
    }

    public void getEntity() {
        // zombie dog
        EntityRegistry.registerGlobalEntityID(EntityZombieDog.class, "GSZombieDog", EntityRegistry.findGlobalUniqueEntityId(), 14144467, 7969893);

        if (GraveStoneConfig.spawnZombieDogs) {
            EntityRegistry.addSpawn(EntityZombieDog.class, 3, 1, 3, EnumCreatureType.monster,
                    BiomeGenBase.forest, BiomeGenBase.forestHills,
                    BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.taigaHills);
        }

        // zombie cat
        EntityRegistry.registerGlobalEntityID(EntityZombieCat.class, "GSZombieCat", EntityRegistry.findGlobalUniqueEntityId(), 15720061, 7969893);

        if (GraveStoneConfig.spawnZombieCats) {
            EntityRegistry.addSpawn(EntityZombieCat.class, 3, 1, 3, EnumCreatureType.monster,
                    BiomeGenBase.jungle, BiomeGenBase.jungleHills);
        }

        // skeleton dog
        EntityRegistry.registerGlobalEntityID(EntitySkeletonDog.class, "GSSkeletonDog", EntityRegistry.findGlobalUniqueEntityId(), 14144467, 4802889);

        if (GraveStoneConfig.spawnSkeletonDogs) {
            EntityRegistry.addSpawn(EntityZombieDog.class, 3, 1, 3, EnumCreatureType.monster,
                    BiomeGenBase.forest, BiomeGenBase.forestHills,
                    BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.taigaHills);
        }

        // skeleton cat
        EntityRegistry.registerGlobalEntityID(EntitySkeletonCat.class, "GSSkeletonCat", EntityRegistry.findGlobalUniqueEntityId(), 15720061, 4802889);

        if (GraveStoneConfig.spawnSkeletonCats) {
            EntityRegistry.addSpawn(EntityZombieCat.class, 3, 1, 3, EnumCreatureType.monster,
                    BiomeGenBase.jungle, BiomeGenBase.jungleHills);
        }
    }
}
