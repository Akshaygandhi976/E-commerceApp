package Models;

public class products
{
    private String ProductName, Description,Price,Pid,Image,Category,Date,Time;
 public  products(){

 }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public products(String productName, String description, String price, String pid, String image, String category, String date, String time) {
        ProductName = productName;
        Description = description;
        Price = price;
        Pid = pid;
        Image = image;
        Category = category;
        Date = date;
        Time = time;
    }
}
