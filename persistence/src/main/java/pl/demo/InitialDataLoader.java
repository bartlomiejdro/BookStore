package pl.demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import pl.demo.entity.Book;
import pl.demo.entity.Privilege;
import pl.demo.entity.User;
import pl.demo.entity.UserRole;
import pl.demo.enumerations.BookCategory;
import pl.demo.enumerations.BookStatus;
import pl.demo.repository.BookRepository;
import pl.demo.repository.PrivilegeRepository;
import pl.demo.repository.UserRepository;
import pl.demo.repository.UserRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private BookRepository bookRepository;

    private UserRepository userRepository;

    private UserRoleRepository userRoleRepository;

    private PrivilegeRepository privilegeRepository;

    //private PasswordEncoder passwordEncoder;
    @Autowired
    public InitialDataLoader(
            BookRepository bookRepository,
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            PrivilegeRepository privilegeRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Books
        createBookIfNotFound(
                "Java dla początkujących",
                new HashSet<>(Arrays.asList("Kacper Koło", "Andżelika Wielka")),
                "Najlepsza książka dla wszystkich zainteresowanych Javą, ale bojących się zapytać",
                BookStatus.FREE,
                new HashSet<>(Arrays.asList(BookCategory.MYSTERY, BookCategory.SELF_HELP)));

        createBookIfNotFound(
                "Kamień i szkło",
                new HashSet<>(Arrays.asList("Kacper Koło", "Andżelika Wielka")),
                "Kamień i szkło to romans, jakiego jeszcze nie czytałeś. Miłość w hucie z wiosną w tle",
                BookStatus.FREE,
                new HashSet<>(Arrays.asList(BookCategory.MYSTERY, BookCategory.SELF_HELP)));
        
        createBookIfNotFound(
                "Pan Lodowego Ogrodu",
                new HashSet<>(Arrays.asList("Jaroslaw Grzedowicz")),
                "Kim  jest pan legendarnego Lodowego Ogrodu?",
                BookStatus.FREE,
                new HashSet<>(Arrays.asList(BookCategory.MYSTERY, BookCategory.SELF_HELP)));
         
        createBookIfNotFound(
                "Siewca Wiatru",
                new HashSet<>(Arrays.asList("Maja Kossakowska")),
                "Książka została przypisana do serii „Anielskie zastępy",
                BookStatus.LOAN,
                new HashSet<>(Arrays.asList(BookCategory.MYSTERY, BookCategory.SELF_HELP)));
        
        createBookIfNotFound(
                "Ani słowa prawdy",
                new HashSet<>(Arrays.asList("Jacek Piekara")),
                "Cykl o Arivaldzie z wybrzeza",
                BookStatus.MISSING,
                new HashSet<>(Arrays.asList(BookCategory.MYSTERY, BookCategory.SELF_HELP)));

        // Privileges
        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        // Roles
        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ADMIN_ROLE", adminPrivileges);
        createRoleIfNotFound("USER_ROLE", Collections.singletonList(readPrivilege));

        // User role
        UserRole adminRole = userRoleRepository.findByName("ADMIN_ROLE");

        // User
        User user = new User();
        user.setUserName("First User");
        //user.setPassword(passwordEncoder.encode("test"));
        user.setPassword("test");
        user.setRoles(Collections.singletonList(adminRole));
        userRepository.save(user);

    }

    @Transactional
    protected void createBookIfNotFound(
            String title,
            Set<String> authors,
            String description,
            BookStatus status,
            Set<BookCategory> categories) {
        List<Book> books = bookRepository.findBookByTitle(title);
        if (books.isEmpty()) {
            Book book = new Book();
            book.setTitle(title);
            book.setAuthors(authors);
            book.setCategories(categories);
            book.setDescription(description);
            book.setStatus(status);
            bookRepository.save(book);
        }
    }

    @Transactional
    protected Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege();
            privilege.setName(name);
            privilege = privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    protected void createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        UserRole role = userRoleRepository.findByName(name);
        if (role == null) {
            role = new UserRole();
            role.setName(name);
            role.setPrivileges(privileges);
            userRoleRepository.save(role);
        }
    }
}
