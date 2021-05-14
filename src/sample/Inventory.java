package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Inventory {


    ImageView inventory;
    ImageView selected;
    boolean loaded = false;
    Vecc2f inventoryPos;
    Vecc2f selectedPos;
    Vecc2f selectedPosUnit = new Vecc2f(0, 0);
    int RES = 68;
    ArrayList<Items> items = new ArrayList<>();
    int invenWIDTH = 5;
    int invenHEIGHT = 6;
    int[][] inventoryPositions = new int[invenWIDTH][invenHEIGHT];
    //39, 39
    //5 x 6 items 30 total

    Texts itemName;
    Texts itemType;
    Texts itemDescription;
    Texts itemEffect;

    public Inventory() {
        this.inventory = new ImageView("file:src\\game\\inventory\\Inventory.png");
        this.inventoryPos = new Vecc2f((float) ((1920 / 2) - (inventory.getBoundsInParent().getWidth() / 2)), (float) ((1080 / 2) - (inventory.getBoundsInParent().getHeight() / 2)));
        this.inventory.relocate(inventoryPos.x, inventoryPos.y);
        this.inventory.setViewOrder(-8);
        //
        this.itemName = new Texts("", ((inventoryPos.x + 431)), (inventoryPos.y + 47), Color.WHITE, 25);
        this.itemType = new Texts("", ((inventoryPos.x + 431)), (inventoryPos.y + 147), Color.ORANGE, 23);
        this.itemDescription = new Texts("", ((inventoryPos.x + 431)), (inventoryPos.y + 247), Color.WHITE, 23);
        this.itemEffect = new Texts("", ((inventoryPos.x + 431)), (inventoryPos.y + 147), Color.ORANGE, 23);
        //
        this.selected = new ImageView("file:src\\game\\inventory\\selected.png");
        this.selectedPos = new Vecc2f((inventoryPos.x + 39), (inventoryPos.y + 39));
        this.selected.relocate(selectedPos.x, selectedPos.y);
        this.selected.setViewOrder(-9);
        for (int i = 0; i < invenWIDTH; i++) {
            for (int j = 0; j < invenHEIGHT; j++) {
                inventoryPositions[i][j] = 0;
            }
        }
    }

    public void useItem(Group group, Hero hero) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).j == selectedPosUnit.x && items.get(i).i == selectedPosUnit.y) {
                itemUser(items.get(i).getName(), hero,items.get(i).getID(),group);
                inventoryPositions[items.get(i).i][items.get(i).j] = 0;
                group.getChildren().remove(items.get(i).getItemImage());
                items.remove(items.get(i));
                textSetter();
            }
        }

    }

    public void addNewItem(Inventory inventory, Group group, int ID, ImageView itemImage, String name, String itemType, String description, String effect) {
        for (int i = 0; i < invenHEIGHT; i++) {
            for (int j = 0; j < invenWIDTH; j++) {
                if (inventoryPositions[i][j] == 0) {
                    inventoryPositions[i][j] = 1;
                    items.add(new Items(inventoryPos, group, ID, j, i, RES, itemImage, name,itemType,description,effect));
                    i = invenHEIGHT;
                    j = invenWIDTH;
                }
            }
        }
    }
    /*
    private void healthIncreasePotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile04.png");
        this.name = "Health Potion";
        this.itemType = "Consumable";
        this.description = "HEALTH";
        this.effect = "+1 heart";
        this.ID = 5;
    }

    private void StrengthPotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile062.png");
        this.name = "Strength Potion";
        this.itemType = "Consumable";
        this.description = "STRONG";
        this.effect = "Increased damage";
        this.ID = 4;

    }

    private void speedPotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile053.png");
        this.name = "Speed Potion";
        this.itemType = "Consumable";
        this.description = " An Invigorating tonic\n that energies the \n eager traveler";
        this.effect = "Increased Speed";
        this.ID = 3;

    }

    private void healthPotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile005.png");
        this.name = "Health Potion";
        this.itemType = "Consumable";
        this.description = " A revitalizing drink to\n heal the wounds of\n weary travelers.";
        this.effect = "+2 Health";
        this.ID = 1;
    }
     */

    private void itemUser(String name, Hero hero, int id, Group group) {
        switch (id) {
            case 1://+2 health
                hero.increaseHealth(2);
                break;
            case 3://speed increase
                hero.setVeloLimit((float) (hero.getVeloLimit() * 1.03));
                break;
            case 4://increase damage
                if (hero.getAttackDamage()<13){
                    hero.setAttackDamage(hero.getAttackDamage()+1);
                }
                break;
            case 5://+1 heart
                hero.addHeart(group);
                break;
        }
    }

    public void moveSelect(String direction) {
        switch (direction) {
            case "up":
                if (selectedPosUnit.y > 0) {
                    selectedPosUnit.y = selectedPosUnit.y - 1;
                }
                break;
            case "down":
                if (selectedPosUnit.y < (invenHEIGHT - 1)) {
                    selectedPosUnit.y = selectedPosUnit.y + 1;
                }
                break;
            case "left":
                if (selectedPosUnit.x > 0) {
                    selectedPosUnit.x = selectedPosUnit.x - 1;
                }
                break;
            case "right":
                if (selectedPosUnit.x < (invenWIDTH - 1)) {
                    selectedPosUnit.x = selectedPosUnit.x + 1;
                }
                break;
        }
        selectedPos.set((inventoryPos.x + 39) + (selectedPosUnit.x * RES), (inventoryPos.y + 39) + (selectedPosUnit.y * RES));
        selected.relocate(selectedPos.x, selectedPos.y);
        textSetter();
    }

    private void textSetter() {
        for (Items item : items) {
            if (item.j == selectedPosUnit.x && item.i == selectedPosUnit.y) {
                this.itemName.setText(item.getName());
                this.itemType.setText(item.getItemType());
                this.itemDescription.setText(item.getDescription());
                this.itemEffect.setText(item.getEffect());
                break;
            } else {
                this.itemName.setText("");
                this.itemType.setText("");
                this.itemDescription.setText("");
                this.itemEffect.setText("");
            }
        }
        if (items.size() == 0) {
            this.itemName.setText("");
            this.itemType.setText("");
            this.itemDescription.setText("");
            this.itemEffect.setText("");
        }
        this.itemName.setPosition((float) ((inventoryPos.x + 431) + ((252 - this.itemName.getText().getBoundsInParent().getWidth()) / 2)), (float) ((inventoryPos.y + 47) + ((47 - this.itemName.getText().getBoundsInParent().getHeight()) / 2)));
        this.itemType.setPosition((inventoryPos.x + 431), (inventoryPos.y + 47) + 70);
        this.itemDescription.setPosition((inventoryPos.x + 431) - 5, (inventoryPos.y + 47) + 110);
        this.itemEffect.setPosition((inventoryPos.x + 431), (inventoryPos.y + 47) + 220);
    }

    public void setLoaded(boolean loaded, Group group) {
        this.loaded = loaded;
        if (this.loaded) {
            load(group);
        } else {
            unload(group);
        }
    }

    public void load(Group group) {
        group.getChildren().addAll(this.inventory, this.selected, this.itemName.getText(), this.itemType.getText(), this.itemDescription.getText(), this.itemEffect.getText());
        selectedPosUnit.set(0, 0);
        selected.relocate((inventoryPos.x + 39), (inventoryPos.y + 39));
        //
        textSetter();
        //
        for (Items item : items) {
            group.getChildren().add(item.getItemImage());
            item.getItemImage().relocate(item.position.x, item.position.y);
            item.getItemImage().setViewOrder(-10);
        }
    }

    public void unload(Group group) {
        group.getChildren().removeAll(this.inventory, this.selected, this.itemName.getText(), this.itemType.getText(), this.itemDescription.getText(), this.itemEffect.getText());
        for (Items item : items) {
            group.getChildren().remove(item.getItemImage());
        }
    }

    public boolean isLoaded() {
        return loaded;
    }
}