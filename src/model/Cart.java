package model;

public class Cart {
	private int cartID;
	private int customerID;
	private String productID;
	private int Quantity;
	public Cart(int cartID, int customerID, String productID, int quantity) {
		super();
		this.cartID = cartID;
		this.customerID = customerID;
		this.productID = productID;
		Quantity = quantity;
	}
	public int getCartID() {
		return cartID;
	}
	public void setCartID(int cartID) {
		this.cartID = cartID;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	@Override
	public String toString() {
		return "Cart [cartID=" + cartID + ", customerID=" + customerID + ", productID=" + productID + ", Quantity="
				+ Quantity + "]";
	}
	
}
