package au.com.accountsflow.object;

import java.util.ArrayList;

public class ProductGroup implements Comparable<ProductGroup>{

	private String name;
    private ArrayList<Product> products;
     
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Product> getProducts() {
        return products;
    }
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
    @Override
	public int compareTo(ProductGroup arg0) {
		return this.getName().compareTo(arg0.getName());
	}
}
