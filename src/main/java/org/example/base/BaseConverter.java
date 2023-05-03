package org.example.base;

public abstract class BaseConverter<TO,PO> {
    public abstract TO convertT(PO po);
    public abstract PO convertP(TO to);

    public TO po2to(PO po){
        return this.convertT(po);
    }

    public PO to2po(TO to){
        return this.convertP(to);
    }

}
