package models;


import javax.persistence.*;

@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class User extends Model {

    // change getAttributeNames() !
    private String name;
    private String email;
    private String address;


    public static final String getAttributeNames() {
        // all DB columns but not id, email, password (they are used separately for login)
        return "name,address,type";
    }

    public String getType() {
        String[] classnames = this.getClass().toString().split("\\.");
        String classname = classnames[classnames.length - 1];
        return classname;
    }

    public boolean isSupplier() {
        return getType().equals("Supplier");
    }

    public boolean isBeneficiary() {
        return getType().equals("Beneficiary");
    }

    public boolean hasWebInterface() {
        return isSupplier() || isBeneficiary();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
