package net.leozeballos.FastFood.foodorder;

import lombok.*;
import net.leozeballos.FastFood.foodorderdetail.FoodOrderDetail;
import net.leozeballos.FastFood.foodorderstatemachine.FoodOrderEvent;
import net.leozeballos.FastFood.foodorderstatemachine.FoodOrderState;
import net.leozeballos.FastFood.branch.Branch;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.statemachine.StateMachine;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FoodOrder {

    /**
     * The unique identifier of the food order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The creation date and time of the food order.
     */
    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    /**
     * The payment date and time of the food order.
     */
    @Column
    private LocalDateTime paymentTimestamp;

    /**
     * The status of the food order.
     */
    @Enumerated(EnumType.STRING)
    private FoodOrderState state;

    /**
     * The branch that the food order is for.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    /**
     * The food order details of the food order.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<FoodOrderDetail> foodOrderDetails = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FoodOrder foodOrder = (FoodOrder) o;
        return id != null && Objects.equals(id, foodOrder.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Returns the total price of the food order.
     * @return double The total price of the food order
     */
    public double calculateTotal() {
        double total = 0.0;
        for (FoodOrderDetail foodOrderDetail : foodOrderDetails) {
            total += foodOrderDetail.getQuantity() * foodOrderDetail.getItem().calculatePrice();
        }
        return total;
    }

    /**
     * Returns the formatted creation timestamp.
     * @return String The formatted creation timestamp
     */
    public String getFormattedCreationTimestamp() {
        return creationTimestamp == null ? "Not set" : creationTimestamp.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
    }

    /**
     * Returns the formatted payment timestamp.
     * @return String The formatted payment timestamp
     */
    public String getFormattedPaymentTimestamp() {
        return paymentTimestamp == null ? "Not set" : paymentTimestamp.format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"));
    }

    /**
     * Returns the formatted order details. E.g. "1 x Burger, 1 x Fries, 1 x Drink"
     * @return String The formatted order details
     */
    public String getFormattedFoodOrderDetails() {
        // formats the food order details into a string for display, e.g. "1 x Burger, 1 x Fries"
        StringBuilder formattedFoodOrderDetails = new StringBuilder();
        for (FoodOrderDetail foodOrderDetail : foodOrderDetails) {
            formattedFoodOrderDetails.append(foodOrderDetail.getQuantity()).append(" x ").append(foodOrderDetail.getItem().getName()).append(", ");
        }
        // removes the last comma and space
        formattedFoodOrderDetails.delete(formattedFoodOrderDetails.length() - 2, formattedFoodOrderDetails.length());
        return formattedFoodOrderDetails.toString();
    }

    /**
     * Returns the formatted total.
     * @return String The formatted total price. E.g. "Total: $10,00"
     */
    public String getFormattedTotal() {
        return "$" + String.format("%.2f", calculateTotal());
    }

    /**
     * Returns the formatted state.
     * @return String The formatted state
     */
    public String getFormattedState() {
        return state.toString().substring(0, 1).toUpperCase() + state.toString().substring(1).toLowerCase();
    }

}