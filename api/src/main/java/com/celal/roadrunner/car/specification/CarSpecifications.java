package com.celal.roadrunner.car.specification;

import com.celal.roadrunner.car.dto.CarSearchParamsDTO;
import com.celal.roadrunner.car.entity.CarEntity;
import com.celal.roadrunner.car.entity.FuelType;
import com.celal.roadrunner.car.entity.TransmissionType;
import com.celal.roadrunner.car.entity.VehicleType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class CarSpecifications {

    private static final char LIKE_ESCAPE = '\\';

    private CarSpecifications() {
    }

    public static Specification<CarEntity> withFilters(CarSearchParamsDTO params) {
        return Specification.allOf(
                containsText(params.getQ()),
                hasPlate(params.getPlate()),
                hasBrand(params.getBrand()),
                hasModel(params.getModel()),
                hasCategory(params.getCategory()),
                hasSuppliers(params.getSuppliers()),
                hasVehicleTypes(params.getVehicleTypes()),
                hasTransmissionTypes(params.getTransmissionTypes()),
                hasFuelTypes(params.getFuelTypes()),
                hasMinSeats(params.getMinSeats()),
                hasMaxSeats(params.getMaxSeats()),
                hasMinDailyPrice(params.getMinDailyPrice()),
                hasMaxDailyPrice(params.getMaxDailyPrice()),
                hasUnlimitedMileage(params.getUnlimitedMileage()),
                hasFlexibleCancellation(params.getFlexibleCancellation()),
                isCarlaCashEligible(params.getCarlaCashEligible()),
                hasCollisionDamageWaiver(params.getCollisionDamageWaiverIncluded()),
                hasTaxesAndFeesIncluded(params.getTaxesAndFeesIncluded()),
                isActive(params.getActive())
        );
    }

    private static Specification<CarEntity> containsText(String value) {
        if (!StringUtils.hasText(value)) {
            return Specification.unrestricted();
        }

        String pattern = containsPattern(value);
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                like(criteriaBuilder, root.get("plate"), pattern),
                like(criteriaBuilder, root.get("brand"), pattern),
                like(criteriaBuilder, root.get("model"), pattern),
                like(criteriaBuilder, root.get("category"), pattern),
                like(criteriaBuilder, root.get("supplier"), pattern)
        );
    }

    private static Specification<CarEntity> hasPlate(String plate) {
        return equalsIgnoreCase("plate", plate);
    }

    private static Specification<CarEntity> hasBrand(String brand) {
        return equalsIgnoreCase("brand", brand);
    }

    private static Specification<CarEntity> hasModel(String model) {
        return equalsIgnoreCase("model", model);
    }

    private static Specification<CarEntity> hasCategory(String category) {
        return equalsIgnoreCase("category", category);
    }

    private static Specification<CarEntity> hasSuppliers(Set<String> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return Specification.unrestricted();
        }

        Set<String> normalized = suppliers.stream()
                .filter(StringUtils::hasText)
                .map(CarSpecifications::normalize)
                .collect(Collectors.toSet());

        if (normalized.isEmpty()) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lower(root.get("supplier")).in(normalized);
    }

    private static Specification<CarEntity> hasVehicleTypes(Set<VehicleType> vehicleTypes) {
        return in("vehicleType", vehicleTypes);
    }

    private static Specification<CarEntity> hasTransmissionTypes(
            Set<TransmissionType> transmissionTypes
    ) {
        return in("transmissionType", transmissionTypes);
    }

    private static Specification<CarEntity> hasFuelTypes(Set<FuelType> fuelTypes) {
        return in("fuelType", fuelTypes);
    }

    private static Specification<CarEntity> hasMinSeats(Integer minSeats) {
        return greaterThanOrEqualTo("seatCount", minSeats);
    }

    private static Specification<CarEntity> hasMaxSeats(Integer maxSeats) {
        return lessThanOrEqualTo("seatCount", maxSeats);
    }

    private static Specification<CarEntity> hasMinDailyPrice(BigDecimal minDailyPrice) {
        return greaterThanOrEqualTo("dailyPrice", minDailyPrice);
    }

    private static Specification<CarEntity> hasMaxDailyPrice(BigDecimal maxDailyPrice) {
        return lessThanOrEqualTo("dailyPrice", maxDailyPrice);
    }

    private static Specification<CarEntity> hasUnlimitedMileage(Boolean unlimitedMileage) {
        return equalsValue("unlimitedMileage", unlimitedMileage);
    }

    private static Specification<CarEntity> hasFlexibleCancellation(Boolean flexibleCancellation) {
        return equalsValue("flexibleCancellation", flexibleCancellation);
    }

    private static Specification<CarEntity> isCarlaCashEligible(Boolean carlaCashEligible) {
        return equalsValue("carlaCashEligible", carlaCashEligible);
    }

    private static Specification<CarEntity> hasCollisionDamageWaiver(Boolean included) {
        return equalsValue("collisionDamageWaiverIncluded", included);
    }

    private static Specification<CarEntity> hasTaxesAndFeesIncluded(Boolean included) {
        return equalsValue("taxesAndFeesIncluded", included);
    }

    private static Specification<CarEntity> isActive(Boolean active) {
        return equalsValue("active", active);
    }

    private static Specification<CarEntity> equalsIgnoreCase(String field, String value) {
        if (!StringUtils.hasText(value)) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                criteriaBuilder.lower(root.get(field)),
                normalize(value)
        );
    }

    private static <T> Specification<CarEntity> in(String field, Set<T> values) {
        if (values == null || values.isEmpty()) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) -> root.get(field).in(values);
    }

    private static <T extends Comparable<? super T>> Specification<CarEntity> greaterThanOrEqualTo(
            String field,
            T value
    ) {
        if (value == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
    }

    private static <T extends Comparable<? super T>> Specification<CarEntity> lessThanOrEqualTo(
            String field,
            T value
    ) {
        if (value == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(field), value);
    }

    private static <T> Specification<CarEntity> equalsValue(String field, T value) {
        if (value == null) {
            return Specification.unrestricted();
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(field), value);
    }

    private static Predicate like(
            CriteriaBuilder criteriaBuilder,
            Expression<String> expression,
            String pattern
    ) {
        return criteriaBuilder.like(
                criteriaBuilder.lower(expression),
                pattern,
                LIKE_ESCAPE
        );
    }

    private static String containsPattern(String value) {
        String escaped = normalize(value)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + escaped + "%";
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
