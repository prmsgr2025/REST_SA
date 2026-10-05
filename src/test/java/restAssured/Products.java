package restAssured;

public record Products(
    int id,
    String name,
    String description,
    double price,
    int category_Id,
    String category_Name
    )
{
    public Products( String name, String description, double price, int category_Id) {
        this(0,name,description,price, category_Id, null);
    }
}
