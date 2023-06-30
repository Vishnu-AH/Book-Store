package com.pace.bookstore.service;

import com.pace.bookstore.dto.*;
import com.pace.bookstore.entity.*;
import com.pace.bookstore.exceptions.BookStoreException;
import com.pace.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleXrefRepository userRoleXrefRepository;

    private final BookRepository bookRepository;

    private final CategoriesRepository categoriesRepository;

    private final CartRepository cartRepository;

    private final BookCategoryXrefRepository bookCategoryXrefRepository;

    private final OrdersRepository ordersRepository;

    private final BooksSoldRepository booksSoldRepository;


    @Override
    public void signUp(SignUpDTO signUpDTO) throws BookStoreException {
        if (!isStrongPassword(signUpDTO.getPassword())) {
            throw new BookStoreException("Please enter strong password, at least one uppercase letter, " + "one lowercase letter, one digit, and one special character needed");
        }

        Optional<Role> optionalRole = roleRepository.findRoleByName(signUpDTO.getRole());
        if (!optionalRole.isPresent()) {
            throw new BookStoreException("Role does not exists");
        }

        Optional<User> optionalUser = userRepository.findByEmail(signUpDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new BookStoreException("User Already registered please sign in");
        } else {
            User user = new User();
            user.setName(signUpDTO.getName());
            user.setEmail(signUpDTO.getEmail().toLowerCase());
            user.setPhoneNumber(signUpDTO.getPhoneNumber());
            user.setActive(true);
            String userId = user.getEmail();

            PasswordEncoder encoder = new BCryptPasswordEncoder(8);
            user.setPassword(encoder.encode(signUpDTO.getPassword()));

            UserRoleXref userRoleXref = new UserRoleXref();
            userRoleXref.setUser(user);
            Optional<Role> roleOptional = roleRepository.findRoleByName(signUpDTO.getRole());
            if (!roleOptional.isPresent()) {
                throw new BookStoreException("Role not found: " + signUpDTO.getRole());
            } else {
                userRoleXref.setRole(roleOptional.get());
                userRoleXref.setActive(true);
                userRepository.save(user);
                userRoleXrefRepository.save(userRoleXref);
            }
        }
    }

    @Override
    public void addCategory(BookCategoryDTO bookCategoryDTO) throws BookStoreException {
        Optional<BookCategory> optionalCategory = categoriesRepository.findByCategoryName(bookCategoryDTO.getCategoryName());
        if (optionalCategory.isPresent()) {
            throw new BookStoreException("Book Category already exists");
        }
        BookCategory bookCategory = BookCategory.builder()
                .categoryName(bookCategoryDTO.getCategoryName())
                .isActive(bookCategoryDTO.isActive())
                .build();
        categoriesRepository.save(bookCategory);
    }

    @Override
    public BookCategoryDTO fetchCategoryById(UUID categoryId) throws BookStoreException {
        Optional<BookCategory> optionalCategory = categoriesRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new BookStoreException("BookCategory not found for given Id");
        }
        BookCategoryDTO bookCategoryDTO = BookCategoryDTO.builder()
                .categoryName(optionalCategory.get().getCategoryName())
                .isActive(optionalCategory.get().isActive())
                .build();
        return bookCategoryDTO;
    }

    @Override
    public void updateBookCategory(UUID categoryId, String newName, boolean flag) throws BookStoreException {
        Optional<BookCategory> optionalCategory = categoriesRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new BookStoreException("BookCategory not found for given Id " + categoryId);
        }
        BookCategory bookCategory = BookCategory.builder()
                .id(optionalCategory.get().getId())
                .categoryName(newName)
                .isActive(flag)
                .build();
        categoriesRepository.save(bookCategory);
    }

    @Override
    public Page<BookCategory> getAllTheBookCategories(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return categoriesRepository.findAll(pageable);
    }

    @Override
    public void addBooks(BookDTO bookDto) throws BookStoreException {
        Optional<BookCategory> optionalCategory = categoriesRepository.findById(bookDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            throw new BookStoreException("Category doesn't exist");
        }
        Role role = userRoleXrefRepository.findRoleByUserId(bookDto.getUserId());
        if (role.getName().equals("buyer")) {
            throw new BookStoreException("Sorry.! Only authors are allowed to add books");
        }

        List<Books> booksList = bookCategoryXrefRepository.findBooksByCategoryId(bookDto.getCategoryId(), bookDto.getBookName());
        for (Books books : booksList) {
            if (books.getBookName().equals(bookDto.getBookName())) {
                throw new BookStoreException("Book name already exists for this category");
            }
        }
        Optional<Books> optionalBook = bookRepository.findByAuthorName(bookDto.getAuthorName(), bookDto.getBookName());

        if (optionalBook.isPresent()) {

            Optional<BookCategoryXref> optionalBookCategoryXref = categoriesRepository.findByBookIdCategoryId(optionalBook.get().getId(), optionalCategory.get().getId());
            if (!optionalBookCategoryXref.isPresent()) {
                throw new BookStoreException("No Book found for given Category");
            }

            if (optionalBook.get().isActive()) {
                throw new BookStoreException("Book already exists for the author name: " + bookDto.getAuthorName());
            }
            Books books = Books.builder()
                    .id(optionalBook.get().getId())
                    .bookName(optionalBook.get().getBookName())
                    .authorName(optionalBook.get().getAuthorName())
                    .price(optionalBook.get().getPrice())
                    .isActive(true)
                    .build();
            bookRepository.save(books);

            BookCategoryXref bookCategoryXref = BookCategoryXref.builder()
                    .id(optionalBookCategoryXref.get().getId())
                    .books(optionalBookCategoryXref.get().getBooks())
                    .category(optionalBookCategoryXref.get().getCategory())
                    .isActive(true)
                    .build();
            bookCategoryXrefRepository.save(bookCategoryXref);
        } else {
            Books book = Books.builder()
                    .bookName(bookDto.getBookName())
                    .price(bookDto.getPrice())
                    .authorName(bookDto.getAuthorName())
                    .isActive(true)
                    .build();
            bookRepository.save(book);

            BookCategoryXref bookCategoryXref = BookCategoryXref.builder()
                    .books(book)
                    .category(optionalCategory.get())
                    .isActive(true)
                    .build();
            bookCategoryXrefRepository.save(bookCategoryXref);
        }
    }

    @Override
    public BookDTO getBookById(UUID id) throws BookStoreException {
        Optional<Books> optionalBook = bookRepository.findById(id);
        if (!optionalBook.isPresent()) {
            throw new BookStoreException("Book not found for given id : " + id);
        }
        BookDTO bookDto = BookDTO.builder()
                .authorName(optionalBook.get().getAuthorName())
                .bookName(optionalBook.get().getBookName())
                .price(optionalBook.get().getPrice())
                .isActive(optionalBook.get().isActive())
                .build();
        return bookDto;
    }

    @Override
    public Page<Books> getAllTheBooks(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return bookRepository.findAll(pageable);
    }

    @Override
    public void updateBook(UUID bookId, double price) throws BookStoreException {
        Optional<Books> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new BookStoreException("Book not found for given id : " + bookId);
        }
        Books books = Books.builder()
                .id(optionalBook.get().getId())
                .bookName(optionalBook.get().getBookName())
                .authorName(optionalBook.get().getAuthorName())
                .price(price)
                .isActive(true)
                .build();
        bookRepository.save(books);
    }

    @Override
    public void deleteBook(UUID bookId, UUID categoryId) throws BookStoreException {
        Optional<Books> optionalBook = bookRepository.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new BookStoreException("Book not found for given id : " + bookId);
        }
        Optional<BookCategoryXref> optionalBookCategoryXref = categoriesRepository.findByBookIdCategoryId(bookId, categoryId);
        if (!optionalBookCategoryXref.isPresent()) {
            throw new BookStoreException("No Book found for given Category");
        }
        Books books = Books.builder()
                .id(optionalBook.get().getId())
                .bookName(optionalBook.get().getBookName())
                .authorName(optionalBook.get().getAuthorName())
                .price(optionalBook.get().getPrice())
                .isActive(false)
                .build();
        bookRepository.save(books);

        BookCategoryXref bookCategoryXref = BookCategoryXref.builder()
                .id(optionalBookCategoryXref.get().getId())
                .category(optionalBookCategoryXref.get().getCategory())
                .books(optionalBookCategoryXref.get().getBooks())
                .isActive(false)
                .build();
        bookCategoryXrefRepository.save(bookCategoryXref);
    }

    @Override
    public List<Books> findAllBookByCategory(UUID categoryId, int pageNumber, int pageSize) throws BookStoreException {
        Optional<BookCategory> optionalCategory = categoriesRepository.findById(categoryId);
        if (!optionalCategory.isPresent()) {
            throw new BookStoreException("Category not present for given id " + categoryId);
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return bookCategoryXrefRepository.findAllBooksByCategoryId(pageable, categoryId);
    }


    @Override
    public double addCart(CartRequestDTO cartRequestDTO) throws BookStoreException {
        Optional<User> optionalUser = userRepository.findById(cartRequestDTO.getUser());
        if (!optionalUser.isPresent()) {
            throw new BookStoreException("User " + cartRequestDTO.getUser() + " is not present");
        }
        Optional<Books> optionalBook = bookRepository.findById(cartRequestDTO.getBooks());
        if (!optionalBook.isPresent()) {
            throw new BookStoreException("Book " + cartRequestDTO.getBooks() + " is not present");
        }
        Optional<Cart> optionalCart = cartRepository.findCartByUserIdBookId(cartRequestDTO.getUser(), cartRequestDTO.getBooks());
        if (optionalCart.isPresent()) {
            int existingQuantity = optionalCart.get().getQuantity();
            int updatedQuantity = existingQuantity + cartRequestDTO.getQuantity();

            double totalPrice = updatedQuantity * optionalBook.get().getPrice();

            Cart cart = Cart.builder()
                    .id(optionalCart.get().getId())
                    .user(optionalCart.get().getUser())
                    .books(optionalCart.get().getBooks())
                    .quantity(updatedQuantity)
                    .totalPrice(totalPrice)
                    .build();
            cartRepository.save(cart);
        } else {
            double totalPrice = cartRequestDTO.getQuantity() * optionalBook.get().getPrice();

            Cart cart = Cart.builder()
                    .user(optionalUser.get())
                    .books(optionalBook.get())
                    .quantity(cartRequestDTO.getQuantity())
                    .totalPrice(totalPrice)
                    .build();
            cartRepository.save(cart);
        }
        return paymentCalculator(optionalUser.get().getId());
    }

    @Override
    public Page<Cart> fetchCart(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return cartRepository.findAll( pageable);
    }

    @Override
    public void deleteCart(UUID cartId) throws BookStoreException {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (!optionalCart.isPresent()) {
            throw new BookStoreException("No cart found for given Id");
        }
        cartRepository.delete(optionalCart.get());
    }

    @Override
    public String orderConfirmation(OrderCartDTO orderCartDTO) throws BookStoreException {
        Optional<Books> optionalBook = cartRepository.findCartByBookId(orderCartDTO.getBookId());
        if (!optionalBook.isPresent()) {
            throw new BookStoreException("Book not present in cart");
        }

        Optional<User> optionalUser = cartRepository.findUserByUserId(orderCartDTO.getUserId());
        if (!optionalUser.isPresent()) {
            throw new BookStoreException("User not present ");
        }

        double payment = paymentCalculator(orderCartDTO.getUserId());
        if (payment < orderCartDTO.getAmount() || payment > orderCartDTO.getAmount()) {
            throw new BookStoreException("Enter proper payable amount");
        } else {
            List<Cart> cartList = cartRepository.findCartByUserId(orderCartDTO.getUserId());
            for (Cart cart : cartList) {
                Orders orders = Orders.builder()
                        .books(optionalBook.get())
                        .user(optionalUser.get())
                        .address(orderCartDTO.getAddress())
                        .build();
                ordersRepository.save(orders);

                Optional<BooksSold> optionalBooksSold = booksSoldRepository.findByBookId(cart.getBooks().getId());
                if (optionalBooksSold.isPresent()) {
                    int soldQuantity = optionalBooksSold.get().getQuantity() + cart.getQuantity();
                    BooksSold booksSold = BooksSold.builder()
                            .id(optionalBooksSold.get().getId())
                            .books(optionalBooksSold.get().getBooks())
                            .quantity(soldQuantity).build();
                    booksSoldRepository.save(booksSold);
                } else {
                    BooksSold booksSold = BooksSold.builder()
                            .books(cart.getBooks())
                            .quantity(cart.getQuantity())
                            .build();
                    booksSoldRepository.save(booksSold);
                }

            }
            return "Payment completed...Order Confirmed";
        }
    }

    @Override
    public List<Orders> findOrderByDateRange(Date startDate, Date endDate) {
        List<Orders> orders = ordersRepository.getOrdersUsingRangeOfDate(startDate, endDate);
        return orders;
    }


    @Override
    public Books findMostSoldBook() throws BookStoreException {
        List<Books> booksList = booksSoldRepository.findMostSoldBook();
        if (booksList.isEmpty()) {
            throw new BookStoreException("No books sold");
        }
        return booksList.get(0);
    }

    @Override
    public List<User> findBookPurchases(UUID bookId) throws BookStoreException {
        List<User> usersList = ordersRepository.findUsersByBookId(bookId);
        if (usersList.isEmpty()) {
            throw new BookStoreException("No orders placed for given book id " + bookId);
        }
        return usersList;
    }


    public double paymentCalculator(UUID userId) {
        List<Cart> cartList = cartRepository.findCartByUserId(userId);
        double payableAmount = 0;
        for (Cart cart : cartList) {
            payableAmount = payableAmount + cart.getTotalPrice();
        }
        return payableAmount;
    }

    private boolean isSpecialChar(char ch) {
        String specialChars = "!@#$%^&*()_-+=[{]};:<>|./?";
        return specialChars.contains(Character.toString(ch));
    }

    private boolean isStrongPassword(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (isSpecialChar(ch)) {
                hasSpecialChar = true;
            }
        }

        return password.length() >= 8 && hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

}
