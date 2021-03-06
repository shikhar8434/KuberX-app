package com.spacebux.kuberx.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallet implements Parcelable {
    private String title;
    private Double target;
    private Double shifts;
    private Integer shiftType;
    private Double current = 0d;
    private String id;

    public Wallet() {
    }

    protected Wallet(Parcel in) {
        title = in.readString();
        if (in.readByte() == 0) {
            target = null;
        } else {
            target = in.readDouble();
        }
        if (in.readByte() == 0) {
            shifts = null;
        } else {
            shifts = in.readDouble();
        }
        if (in.readByte() == 0) {
            current = null;
        } else {
            current = in.readDouble();
        }
        id = in.readString();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }

    public Double getShifts() {
        return shifts;
    }

    public void setShifts(Double shifts) {
        this.shifts = shifts;
    }

    public Integer getShiftType() {
        return shiftType;
    }

    public void setShiftType(Integer shiftType) {
        this.shiftType = shiftType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        if (target == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(target);
        }
        if (shifts == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(shifts);
        }
        if (current == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(current);
        }
        dest.writeString(id);
    }


}
