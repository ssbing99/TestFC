package my.com.taruc.fitnesscompanion.Classes;

/**
 * Created by seebi_000 on 11/1/2016.
 */

public class Food {
    String id;
    String name;
    String category;
    String nutient;
    String amount;
    String unit;

    public String getNutient() {
        return nutient;
    }

    public void setNutient(String nutient) {
        this.nutient = nutient;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
