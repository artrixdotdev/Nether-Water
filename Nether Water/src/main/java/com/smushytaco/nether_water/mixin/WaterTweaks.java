package com.smushytaco.nether_water.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.smushytaco.nether_water.NetherWater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.item.BucketItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BucketItem.class)
public abstract class WaterTweaks {

    @WrapOperation(
            method = "emptyContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;"
            )
    )
    private Object hookPlaceFluid(
            EnvironmentAttributeSystem instance,
            EnvironmentAttribute<?> attribute,
            BlockPos pos,
            Operation<Object> original
    ) {
        if (attribute == EnvironmentAttributes.WATER_EVAPORATES) {
            boolean shouldEvaporate = (Boolean) original.call(instance, attribute, pos);
            return shouldEvaporate && !NetherWater.INSTANCE.canHaveWater(pos.getY());
        }
        return original.call(instance, attribute, pos);
    }
}