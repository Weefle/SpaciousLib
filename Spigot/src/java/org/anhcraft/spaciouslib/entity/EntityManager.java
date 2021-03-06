package org.anhcraft.spaciouslib.entity;

import org.anhcraft.spaciouslib.attribute.Attribute;
import org.anhcraft.spaciouslib.attribute.AttributeModifier;
import org.anhcraft.spaciouslib.nbt.NBTCompound;
import org.anhcraft.spaciouslib.nbt.NBTManager;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A class helps you to manage entities
 */
public class EntityManager {
    private Entity entity;

    /**
     * Creates a new EntityManager instance
     * @param entity the Bukkit entity
     */
    public EntityManager(Entity entity){
        this.entity = entity;
    }

    /**
     * Sets an attribute
     * @param attribute Attribute object
     */
    public void setAttribute(Attribute attribute){
        NBTCompound c = NBTManager.fromEntity(this.entity);
        List<NBTCompound> newAttrs = new ArrayList<>();
        List<NBTCompound> attrs = c.getList("Attributes");
        NBTCompound attr = NBTManager.newCompound();
        if(attrs != null){
            for(NBTCompound a : attrs){
                if(!a.getString("Name").equals(attribute.getType().getID())){
                    newAttrs.add(a);
                }
            }
        }
        attr = attr.setString("Name", attribute.getType().getID())
                .setDouble("Base", attribute.getType().getBaseValue());
        List<NBTCompound> modifiers = new ArrayList<>();
        for(AttributeModifier modifier : attribute.getModifiers()){
            modifiers.add(NBTManager.newCompound()
                    .set("Name", modifier.getName())
            .set("Amount", modifier.getAmount())
            .set("Operation", modifier.getOperation().getID())
            .set("UUIDMost", modifier.getUniqueID().getMostSignificantBits())
            .set("UUIDLeast", modifier.getUniqueID().getLeastSignificantBits()));
        }
        attr = attr.setList("Modifiers", modifiers);
        newAttrs.add(attr);
        c.setList("Attributes", newAttrs).toEntity(this.entity);
    }

    /**
     * Gets all attributes of this entity
     * @return list of all attributes
     */
    public List<Attribute> getAttributes(){
        List<Attribute> attrs = new ArrayList<>();
        List<NBTCompound> nbtattrs = NBTManager.fromEntity(this.entity).getList("Attributes");
        if(nbtattrs == null){
            return attrs;
        }
        for(NBTCompound a : nbtattrs){
            Attribute attr = new Attribute(Attribute.Type.getByID(a.getString("Name")));
            List<NBTCompound> nbtmodifiers = a.getList("Modifiers");
            if(nbtmodifiers != null) {
                for(NBTCompound modifier : nbtmodifiers) {
                    attr.addModifier(new AttributeModifier(new UUID(modifier.getLong("UUIDMost"), modifier.getLong("UUIDLeast")), modifier.getString("Name"), modifier.getDouble("Amount"), AttributeModifier.Operation.getByID(modifier.getInt("Operation"))));
                }
            }
            attrs.add(attr);
        }
        return attrs;
    }
}