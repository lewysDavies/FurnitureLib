package de.Ste3et_C0st.FurnitureLib.main.entity;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.EulerAngle;

import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

import de.Ste3et_C0st.FurnitureLib.NBT.NBTTagCompound;
import de.Ste3et_C0st.FurnitureLib.Utilitis.Relative;
import de.Ste3et_C0st.FurnitureLib.main.FurnitureLib;
import de.Ste3et_C0st.FurnitureLib.main.ObjectID;
import de.Ste3et_C0st.FurnitureLib.main.Type;
import de.Ste3et_C0st.FurnitureLib.main.Type.BodyPart;

public class fArmorStand extends fEntity {

	private int armorstandID;
	private boolean arms=false,small=false,marker=true,baseplate=true;
	private HashMap<BodyPart, EulerAngle> angle = new HashMap<Type.BodyPart, EulerAngle>();
	private ArmorStand entity = null;
	
	public EulerAngle getBodyPose(){return getPose(BodyPart.BODY);}
	public EulerAngle getLeftArmPose(){return getPose(BodyPart.LEFT_ARM);}
	public EulerAngle getRightArmPose(){return getPose(BodyPart.RIGHT_ARM);}
	public EulerAngle getLeftLegPose(){return getPose(BodyPart.LEFT_LEG);}
	public EulerAngle getRightLegPose(){return getPose(BodyPart.RIGHT_LEG);}
	public EulerAngle getHeadPose(){return getPose(BodyPart.HEAD);}
	public fArmorStand setBodyPose(EulerAngle a){setPose(a,BodyPart.BODY);return this;}
	public fArmorStand setLeftArmPose(EulerAngle a){setPose(a,BodyPart.LEFT_ARM);return this;}
	public fArmorStand setRightArmPose(EulerAngle a){setPose(a,BodyPart.RIGHT_ARM);return this;}
	public fArmorStand setLeftLegPose(EulerAngle a){setPose(a,BodyPart.LEFT_LEG);return this;}
	public fArmorStand setRightLegPose(EulerAngle a){setPose(a,BodyPart.RIGHT_LEG);return this;}
	public fArmorStand setHeadPose(EulerAngle a){setPose(a,BodyPart.HEAD);return this;}
	public int getArmorID(){return this.armorstandID;}
	public boolean hasArms(){return this.arms;}
	public boolean hasBasePlate(){return this.baseplate;}
	public boolean isMarker(){return this.marker;}
	public boolean isSmall(){return this.small;}
	
	public boolean isRealEntity(){
		if(entity==null) return false;
		return true;
	}
	
	public void setEntity(Entity entity){
		if(entity instanceof ArmorStand) this.entity = (ArmorStand) entity;
	}
	
	public fArmorStand clone(Relative relative){
		return clone(relative.getSecondLocation());
	}
	
	public fArmorStand(Location loc, ObjectID obj) {
		super(loc, EntityType.ARMOR_STAND, obj);
	}

	public EulerAngle getPose(BodyPart part){
		if(!angle.containsKey(part)){return part.getDefAngle();}
		return angle.get(part);
	}
	
	  public fArmorStand setPose(EulerAngle angle, BodyPart part)
	  {
	    if (angle == null) {
	      return this;
	    }
	    if (part == null) {
	      return this;
	    }
	    this.angle.put(part, angle);
	    angle = FurnitureLib.getInstance().getLocationUtil().Radtodegress(angle);
	    getWatcher().setObject(new WrappedDataWatcherObject(part.getField(), Registry.getVectorSerializer()), new Vector3F((float)angle.getX(),(float)angle.getY(),(float)angle.getZ()));
	    return this;
	  }
	
	public fArmorStand setSmall(boolean b){
		setBitMask(b, Type.field.getBitMask(), 0);
		this.small = b;
		return this;
	}

	public fArmorStand setArms(boolean b) {
		setBitMask(b, Type.field.getBitMask(), 2);
		this.arms = b;
		return this;
	}
	
	  public fArmorStand setBasePlate(boolean b)
	  {
		setBitMask(!b, Type.field.getBitMask(), 3);
	    this.baseplate = b;
	    return this;
	  }
	
	public fArmorStand setMarker(boolean b){
		setBitMask(!b, Type.field.getBitMask(), 4);
		this.marker = b;
		return this;
	}

	public fArmorStand clone(Location location){
		fArmorStand nStand = new fArmorStand(location, getObjID());
		fInventory inv = new fInventory(nStand.getEntityID());
		for(int i = 0; i<7;i++){
			if(getInventory().getSlot(i)==null) continue;
			inv.setSlot(i, getInventory().getSlot(i));
		}
		nStand.setInventory(inv);
		nStand.setSmall(this.isSmall());
		nStand.setInvisible(this.isInvisible());
		nStand.setMarker(this.isMarker());
		nStand.setGlowing(this.isGlowing());
		nStand.setArms(this.hasArms());
		nStand.setBasePlate(this.hasBasePlate());
		nStand.setFire(this.isFire());
		nStand.setName(this.getCustomName());
		nStand.setNameVasibility(this.isCustomNameVisible());
		for(BodyPart part : BodyPart.values()){
			nStand.setPose(this.getPose(part), part);
		}
		FurnitureLib.getInstance().getFurnitureManager().addArmorStand(nStand);
		return nStand;
	}
	
	public ArmorStand toRealEntity(){
		if(entity!=null){if(!entity.isDead()){return entity;}}
		entity = (ArmorStand) getWorld().spawnEntity(getLocation(), getEntityType());
		entity.setArms(this.hasArms());
		entity.setVisible(!this.isInvisible());
		entity.setSmall(isSmall());
		entity.setArms(hasArms());
		entity.setGravity(hasGravity());
		entity.setGlowing(isGlowing());
		entity.setAI(false);
		entity.setHeadPose(getHeadPose());
		entity.setLeftArmPose(getLeftArmPose());
		entity.setRightArmPose(getRightArmPose());
		entity.setLeftLegPose(getLeftLegPose());
		entity.setRightLegPose(getRightLegPose());
		entity.setBodyPose(getBodyPose());
		
		entity.setCustomName(getCustomName());
		entity.setCustomNameVisible(isCustomNameVisible());
		
		entity.setBasePlate(hasBasePlate());
		entity.setHelmet(getHelmet());
		entity.setChestplate(getChestPlate());
		entity.setLeggings(getLeggings());
		entity.setBoots(getBoots());
		return entity;
	}

	public NBTTagCompound getMetaData(){
		getDefNBT(this);
		setMetadata("Arms", this.hasArms());
		setMetadata("BasePlate", this.hasBasePlate());
		setMetadata("Gravity", this.hasGravity());
		setMetadata("Marker", this.isMarker());
		setMetadata("Small", this.isSmall());
		set("EulerAngle", getEulerAngle(this));
		return getNBTField();
	}
	
	@Override
	public void loadMetadata(NBTTagCompound metadata) {
		loadDefMetadata(metadata);
		NBTTagCompound euler = metadata.getCompound("EulerAngle");
		for(BodyPart part : BodyPart.values()){
			this.setPose(eulerAngleFetcher(euler.getCompound(part.toString())), part);
		}
		boolean b = (metadata.getInt("BasePlate")==1),s = (metadata.getInt("Small")==1),a = (metadata.getInt("Arms")==1),m = (metadata.getInt("Marker")==1),grav = false;
		if(metadata.hasKey("Gravity")){
			grav = metadata.getInt("Gravity")==1;
		}	
		this.setBasePlate(b).setSmall(s).setMarker(m).setArms(a).setGravity(grav);
	}
	
	private EulerAngle eulerAngleFetcher(NBTTagCompound eularAngle){
		Double X = eularAngle.getDouble("X");
		Double Y = eularAngle.getDouble("Y");
		Double Z = eularAngle.getDouble("Z");
		return new EulerAngle(X, Y, Z);
	}
}
