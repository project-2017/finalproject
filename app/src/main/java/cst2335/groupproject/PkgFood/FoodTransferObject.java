package cst2335.groupproject.PkgFood;

public class FoodTransferObject implements Comparable<FoodTransferObject> {

    private int foodID;
    private String foodName, foodServings, foodCalories, foodFat, foodCarbohydrate, foodDate, foodTime;

    /**
     * Constructor
     *
     * @param foodID food column id
     * @param foodName the food name
     * @param foodServings servings
     * @param foodCalories food calories
     * @param foodFat food fat
     * @param foodCarbohydrate food carbohydrate
     * @param foodDate the date food eaten
     * @param foodTime the time food eaten
     */
    public FoodTransferObject(int foodID, String foodName, String foodServings, String foodCalories, String foodFat, String foodCarbohydrate, String foodDate, String foodTime) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodServings = foodServings;
        this.foodCalories = foodCalories;
        this.foodFat = foodFat;
        this.foodCarbohydrate = foodCarbohydrate;
        this.foodDate = foodDate;
        this.foodTime = foodTime;
    }

    public int getFoodID() {
        return foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodServings() {
        return foodServings;
    }

    public String getFoodCalories() {
        return foodCalories;
    }

    public String getFoodFat() {
        return foodFat;
    }

    public String getFoodCarbohydrate() {
        return foodCarbohydrate;
    }

    public String getFoodDate() {
        return foodDate;
    }

    public String getFoodTime() {
        return foodTime;
    }


    /**
     * A function for sorting arrayList
     *
     * @param object The food list information
     * @return compared result
     */
    @Override
    public int compareTo(FoodTransferObject object) {
        return (object.getFoodDate() + " " + object.getFoodTime()).compareTo(getFoodDate() + " " + getFoodTime());
    }

}