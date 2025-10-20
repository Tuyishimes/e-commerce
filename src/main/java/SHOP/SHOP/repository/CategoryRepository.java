package SHOP.SHOP.repository;

import SHOP.SHOP.model.Product;
import SHOP.SHOP.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {}
