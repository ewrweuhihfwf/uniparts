package com.example.parts.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class VehicleCatalogService {

    private final Map<String, List<String>> brandModelMap;

    public VehicleCatalogService() {
        LinkedHashMap<String, List<String>> catalog = new LinkedHashMap<>();

        addBrand(catalog, "Acura", "ILX", "Integra", "MDX", "NSX", "RDX", "RLX", "TLX");
        addBrand(catalog, "Alfa Romeo", "Giulia", "Giulietta", "MiTo", "Stelvio", "Tonale");
        addBrand(catalog, "Audi", "A3", "A4", "A6", "A8", "Q3", "Q5", "Q7", "Q8", "TT");
        addBrand(catalog, "BMW", "1 Series", "3 Series", "5 Series", "7 Series", "M3", "M5", "X1", "X3", "X5", "X6", "X7", "Z4");
        addBrand(catalog, "Chevrolet", "Camaro", "Captiva", "Cruze", "Equinox", "Malibu", "Silverado", "Tahoe", "Trailblazer");
        addBrand(catalog, "Citroen", "Berlingo", "C3", "C4", "C5", "C-Elysee", "Jumper", "Jumpy");
        addBrand(catalog, "Dacia", "Duster", "Jogger", "Logan", "Sandero", "Spring");
        addBrand(catalog, "Dodge", "Challenger", "Charger", "Dart", "Durango", "Journey", "RAM 1500");
        addBrand(catalog, "Fiat", "500", "500X", "Doblo", "Ducato", "Egea", "Panda", "Tipo");
        addBrand(catalog, "Ford", "Escape", "Explorer", "Fiesta", "Focus", "Fusion", "Mustang", "Ranger", "Transit");
        addBrand(catalog, "Genesis", "G70", "G80", "G90", "GV60", "GV70", "GV80");
        addBrand(catalog, "GMC", "Acadia", "Canyon", "Savana", "Sierra", "Terrain", "Yukon");
        addBrand(catalog, "Honda", "Accord", "Civic", "CR-V", "Fit", "HR-V", "Insight", "Odyssey", "Pilot");
        addBrand(catalog, "Hyundai", "Accent", "Elantra", "Genesis", "i30", "Kona", "Santa Fe", "Sonata", "Tucson");
        addBrand(catalog, "Infiniti", "EX", "FX", "G35", "G37", "Q50", "Q60", "QX50", "QX60", "QX80");
        addBrand(catalog, "Isuzu", "D-Max", "MU-X", "NPR", "Rodeo", "Trooper");
        addBrand(catalog, "Jaguar", "E-Pace", "F-Pace", "F-Type", "I-Pace", "XE", "XF", "XJ");
        addBrand(catalog, "Jeep", "Cherokee", "Compass", "Gladiator", "Grand Cherokee", "Renegade", "Wrangler");
        addBrand(catalog, "Kia", "Carnival", "Ceed", "K5", "Mohave", "Optima", "Picanto", "Rio", "Sorento", "Soul", "Sportage", "Stinger");
        addBrand(catalog, "Land Rover", "Defender", "Discovery", "Discovery Sport", "Freelander", "Range Rover", "Range Rover Evoque", "Range Rover Sport", "Velar");
        addBrand(catalog, "Lexus", "ES", "GS", "GX", "IS", "LC", "LS", "LX", "NX", "RX", "UX");
        addBrand(catalog, "Mazda", "CX-3", "CX-5", "CX-9", "Mazda2", "Mazda3", "Mazda6", "MX-5");
        addBrand(catalog, "Mercedes-Benz", "A-Class", "C-Class", "CLA", "CLS", "E-Class", "G-Class", "GLA", "GLC", "GLE", "GLS", "S-Class");
        addBrand(catalog, "MINI", "Clubman", "Cooper", "Countryman", "Paceman");
        addBrand(catalog, "Mitsubishi", "ASX", "Eclipse Cross", "L200", "Lancer", "Outlander", "Pajero");
        addBrand(catalog, "Nissan", "Altima", "Armada", "Juke", "Leaf", "Maxima", "Murano", "Pathfinder", "Qashqai", "Rogue", "Sentra", "Versa", "X-Trail");
        addBrand(catalog, "Opel", "Astra", "Corsa", "Insignia", "Mokka", "Vectra", "Zafira");
        addBrand(catalog, "Peugeot", "208", "308", "408", "508", "2008", "3008", "5008", "Partner");
        addBrand(catalog, "Porsche", "718 Boxster", "718 Cayman", "Cayenne", "Macan", "Panamera", "Taycan");
        addBrand(catalog, "Renault", "Clio", "Duster", "Kadjar", "Kangoo", "Logan", "Megane", "Sandero", "Talisman");
        addBrand(catalog, "SEAT", "Alhambra", "Arona", "Ateca", "Ibiza", "Leon", "Toledo");
        addBrand(catalog, "Skoda", "Fabia", "Kamiq", "Karoq", "Kodiaq", "Octavia", "Rapid", "Superb");
        addBrand(catalog, "Subaru", "BRZ", "Crosstrek", "Forester", "Impreza", "Legacy", "Outback", "WRX");
        addBrand(catalog, "Suzuki", "Alto", "Grand Vitara", "Ignis", "Jimny", "Swift", "SX4", "Vitara");
        addBrand(catalog, "Tesla", "Cybertruck", "Model 3", "Model S", "Model X", "Model Y", "Roadster");
        addBrand(catalog, "Toyota", "4Runner", "Auris", "Avalon", "Camry", "Corolla", "Highlander", "Land Cruiser", "Prius", "RAV4", "Tacoma", "Tundra", "Yaris");
        addBrand(catalog, "Volkswagen", "Amarok", "Arteon", "Caddy", "Golf", "Jetta", "Passat", "Polo", "Tiguan", "Touareg", "Transporter");
        addBrand(catalog, "Volvo", "S60", "S90", "V60", "V90", "XC40", "XC60", "XC90");

        LinkedHashMap<String, List<String>> sortedCatalog = catalog.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> List.copyOf(entry.getValue()),
                        (left, right) -> left,
                        LinkedHashMap::new
                ));

        this.brandModelMap = Collections.unmodifiableMap(sortedCatalog);
    }

    public List<String> getBrands() {
        return brandModelMap.keySet().stream().toList();
    }

    public List<String> getAllModels() {
        return brandModelMap.values().stream()
                .flatMap(List::stream)
                .distinct()
                .sorted()
                .toList();
    }

    public Map<String, List<String>> getBrandModelMap() {
        return brandModelMap;
    }

    public List<Integer> getYearOptions() {
        return IntStream.iterate(2027, year -> year >= 1950, year -> year - 1)
                .boxed()
                .toList();
    }

    private void addBrand(Map<String, List<String>> catalog, String brand, String... models) {
        catalog.put(brand, List.of(models));
    }
}
