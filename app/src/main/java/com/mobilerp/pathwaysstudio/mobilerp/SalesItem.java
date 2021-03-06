package com.mobilerp.pathwaysstudio.mobilerp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eligio Becerra on 25/07/2017.
 * Copyright (C) 2017 Eligio Becerra
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class SalesItem implements Parcelable {
    public static final Creator<SalesItem> CREATOR = new Creator<SalesItem>() {
        @Override
        public SalesItem createFromParcel(Parcel in) {
            return new SalesItem(in);
        }

        @Override
        public SalesItem[] newArray(int size) {
            return new SalesItem[size];
        }
    };

    public int amount;
    public String barcode;
    public String name;
    public Double price;

    public String TABLE_NAME = "Product";
    public String COLUMN_NAME_BARCODE = "barcode";
    public String COLUMN_NAME_AMOUNT = "amount";
    public String COLUMN_NAME_PRICE = "price";
    public String COLUMN_NAME_NAME = "name";

    /**
     * Basic constructor
     *
     * @param barcode
     * @param amount
     * @param price
     * @param name
     */

    public SalesItem(String barcode, int amount, Double price, String name) {
        this.barcode = barcode;
        this.amount = amount;
        this.price = price;
        this.name = name;
    }

    protected SalesItem(Parcel in) {
        barcode = in.readString();
        amount = in.readInt();
        price = in.readDouble();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0    ;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(barcode);
        dest.writeInt(amount);
    }

    public void InsertItem() {

    }
}
