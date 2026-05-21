interface flyweight {
    void operation();
}

class ConcreteFlyweight implements flyweight {
    private String specie_tree;
    private String texture_tree;
    private String maximum_height_tree;

    public ConcreteFlyweight(String specie_tree, String texture_tree, String maximum_height_tree) {
        this.specie_tree = specie_tree;
        this.texture_tree = texture_tree;
        this.maximum_height_tree = maximum_height_tree;
    }

    @Override
    public void operation() {
        System.out.println("Specie: " + specie_tree + ", Texture: " + texture_tree + ", Maximum Height: " + maximum_height_tree);
    }
}

class MutableFlyweight implements flyweight {
    private String actual_height_tree;
    private String diameter_at_breast_height;
    public MutableFlyweight(String actual_height_tree, String diameter_at_breast_height) {
        this.actual_height_tree = actual_height_tree;
        this.diameter_at_breast_height = diameter_at_breast_height;
    }
    public void operation() {
        System.out.println("Actual Height: " + actual_height_tree + ", Diameter at Breast Height: " + diameter_at_breast_height);
    }
}

class FlyweightFactory {
    private Map<String, flyweight> flyweights = new HashMap<>(); 

    public flyweight getFlyweight(String key) {
        if (!flyweights.containsKey(key)) {
            String[] attributes = key.split(",");
            flyweights.put(key, new ConcreteFlyweight(attributes[0], attributes[1], attributes[2]), new MutableFlyweight());
        }
        return flyweights.get(key);
    }
}

public class Main {
    public static void main(String[] args) {
        FlyweightFactory factory = new FlyweightFactory();

        flyweight tree1 = factory.getFlyweight("Oak,Rough,20m");
        flyweight tree2 = factory.getFlyweight("Pine,Smooth,30m");

        tree1.operation();
        tree2.operation();

        MutableFlyweight mutableTree = new MutableFlyweight("15m", "50cm");
        mutableTree.operation();
    }
}