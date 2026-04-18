package com.example.parts.config;

import com.example.parts.domain.AppUser;
import com.example.parts.domain.PartCondition;
import com.example.parts.domain.PartListing;
import com.example.parts.domain.UserRole;
import com.example.parts.repository.AppUserRepository;
import com.example.parts.repository.PartListingRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final AppUserRepository appUserRepository;
    private final PartListingRepository partListingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(AppUserRepository appUserRepository,
                           PartListingRepository partListingRepository,
                           PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.partListingRepository = partListingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        normalizeDemoData();

        if (appUserRepository.count() > 0 || partListingRepository.count() > 0) {
            return;
        }

        AppUser seller = createUser("test", "test@uniparts.ge", "+995 599 11 22 33");

        appUserRepository.save(createUser("buyer", "buyer@uniparts.ge", "+995 555 77 66 44"));

        partListingRepository.save(createListing(
                "Toyota Camry წინა ფარი",
                "Toyota",
                "Camry",
                2018,
                "განათება",
                "ორიგინალი მარცხენა წინა ფარი, იდეალურ მდგომარეობაში.",
                new BigDecimal("320.00"),
                PartCondition.USED,
                seller
        ));
        partListingRepository.save(createListing(
                "BMW F10 ბრემბოს ხუნდები",
                "BMW",
                "F10",
                2014,
                "სამუხრუჭე სისტემა",
                "ახალი ბრემბოს ხუნდები წინა ღერძისთვის. შესაფერისია F10/F11 მოდელებზე.",
                new BigDecimal("180.00"),
                PartCondition.NEW,
                seller
        ));
        partListingRepository.save(createListing(
                "Mercedes W212 სარკე",
                "Mercedes-Benz",
                "E-Class W212",
                2013,
                "კუზაო",
                "მარჯვენა სარკე, გათბობით და ელექტრო დაკეცვით. მცირე კოსმეტიკური ნაკაწრი აქვს.",
                new BigDecimal("260.00"),
                PartCondition.REPLACEMENT,
                seller
        ));

        log.info("Loaded demo users and listings for dev profile");
    }

    private void normalizeDemoData() {
        Optional<AppUser> oldDemoUser = appUserRepository.findByEmail("giorgi@uniparts.ge");
        Optional<AppUser> newDemoUser = appUserRepository.findByEmail("test@uniparts.ge");

        if (oldDemoUser.isPresent() && newDemoUser.isEmpty()) {
            AppUser user = oldDemoUser.get();
            user.setFullName("test");
            user.setEmail("test@uniparts.ge");
            user.setPasswordHash(passwordEncoder.encode("password123"));
            appUserRepository.save(user);
            log.info("Updated old demo user to test@uniparts.ge");
        } else if (newDemoUser.isPresent()) {
            AppUser user = newDemoUser.get();
            if (!"test".equals(user.getFullName())) {
                user.setFullName("test");
                appUserRepository.save(user);
            }
        }

        List<PartListing> listings = partListingRepository.findByOrderByCreatedAtDesc();
        boolean changed = false;
        for (PartListing listing : listings) {
            if (listing.getVehicleYear() == null) {
                if (listing.getTitle().contains("Camry")) {
                    listing.setVehicleYear(2018);
                    listing.setModel("Camry");
                    changed = true;
                } else if (listing.getTitle().contains("BMW F10")) {
                    listing.setVehicleYear(2014);
                    listing.setModel("F10");
                    changed = true;
                } else if (listing.getTitle().contains("W212")) {
                    listing.setVehicleYear(2013);
                    changed = true;
                }
            }
        }
        if (changed) {
            partListingRepository.saveAll(listings);
            log.info("Normalized demo listings with vehicle years");
        }
    }

    private AppUser createUser(String fullName, String email, String phone) {
        AppUser user = new AppUser();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setRole(UserRole.USER);
        return user;
    }

    private PartListing createListing(String title,
                                      String brand,
                                      String model,
                                      Integer vehicleYear,
                                      String partCategory,
                                      String description,
                                      BigDecimal price,
                                      PartCondition condition,
                                      AppUser seller) {
        PartListing listing = new PartListing();
        listing.setTitle(title);
        listing.setBrand(brand);
        listing.setModel(model);
        listing.setVehicleYear(vehicleYear);
        listing.setPartCategory(partCategory);
        listing.setDescription(description);
        listing.setPrice(price);
        listing.setCondition(condition);
        listing.setSeller(seller);
        return listing;
    }
}
