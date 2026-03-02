package rings_of_saturn.github.io.armor_stand_swap.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorStandEntity.class)
public abstract class ArmorStandMixin {

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow
    public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    @Inject(method = "interactAt", at=@At("HEAD"), cancellable = true)
    void swapEquipment(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if(player.isSneaking()){
            ItemStack[] armorStandEquipment = new ItemStack[4];
            for (int i = 0; i < 4; i++) {
                armorStandEquipment[i] = this.getEquippedStack(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i));
            }

            for (int i = 0; i < 4; i++) {
                this.equipStack(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i), player.getEquippedStack(EquipmentSlot.fromTypeIndex(EquipmentSlot.Type.ARMOR, i)));
            }


            for (int i = 0; i < 4; i++) {
                player.getInventory().armor.set(i, armorStandEquipment[i]);
            }
            cir.cancel();
        }
    }
}