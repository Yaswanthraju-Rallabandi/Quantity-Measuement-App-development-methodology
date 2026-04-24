public class Quantity {

    // Enum (base unit = FEET)
    enum LengthUnit {
        FEET(1.0),
        INCHES(1.0 / 12.0),
        YARDS(3.0),
        CENTIMETERS(0.393701 / 12.0);

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }

        public double fromFeet(double feetValue) {
            return feetValue / toFeetFactor;
        }
    }

    // Value Object
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null)
                throw new IllegalArgumentException("Unit cannot be null");

            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Invalid value");

            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        // Convert to another unit
        public QuantityLength convertTo(LengthUnit target) {
            double feet = toFeet();
            double converted = target.fromFeet(feet);
            return new QuantityLength(converted, target);
        }

        // 🔹 UC6: ADDITION METHOD
        public QuantityLength add(QuantityLength other) {
            if (other == null)
                throw new IllegalArgumentException("Other quantity cannot be null");

            double sumFeet = this.toFeet() + other.toFeet();

            double resultValue = this.unit.fromFeet(sumFeet);
            return new QuantityLength(resultValue, this.unit);
        }

        // Static version (optional)
        public static QuantityLength add(QuantityLength a, QuantityLength b, LengthUnit targetUnit) {
            if (a == null || b == null || targetUnit == null)
                throw new IllegalArgumentException("Invalid input");

            double sumFeet = a.toFeet() + b.toFeet();
            double result = targetUnit.fromFeet(sumFeet);

            return new QuantityLength(result, targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;

            QuantityLength other = (QuantityLength) obj;

            return Math.abs(this.toFeet() - other.toFeet()) < 1e-6;
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    // MAIN METHOD (fixes your error)
    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCHES);

        QuantityLength result1 = q1.add(q2);
        System.out.println("1 ft + 12 in = " + result1);

        QuantityLength q3 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.FEET);

        QuantityLength result2 = q3.add(q4);
        System.out.println("12 in + 1 ft = " + result2);

        QuantityLength q5 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q6 = new QuantityLength(3.0, LengthUnit.FEET);

        System.out.println("1 yard + 3 feet = " + q5.add(q6));

        QuantityLength q7 = new QuantityLength(2.54, LengthUnit.CENTIMETERS);
        QuantityLength q8 = new QuantityLength(1.0, LengthUnit.INCHES);

        System.out.println("2.54 cm + 1 inch = " + q7.add(q8));

        // static add with target unit
        QuantityLength result3 = QuantityLength.add(q1, q2, LengthUnit.INCHES);
        System.out.println("Result in inches: " + result3);
    }
}