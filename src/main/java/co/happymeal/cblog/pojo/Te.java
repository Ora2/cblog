package co.happymeal.cblog.pojo;

public class Te {

    private String name;
    private String ps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    @Override
    public String toString() {
        return "Te{" +
                "name='" + name + '\'' +
                ", ps='" + ps + '\'' +
                '}';
    }
}
