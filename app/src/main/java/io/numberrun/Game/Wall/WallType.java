package io.numberrun.Game.Wall;

// 壁の種類
import java.awt.Color;

public enum WallType {
    Add {
        // 通過すると加算される
        @Override
        public Color backgroundColorEnd() {
            // #158adb
            return new Color(21, 138, 219, 240);
        }

        @Override
        public String label() {
            return "+";
        }

        @Override
        public int getAppliedNumber(int base, int with) {
            return base + with;
        }
    },
    Subtract {
        // 通過すると減算される
        @Override
        public Color backgroundColorEnd() {
            // #df3d5b
            return new Color(223, 61, 91, 240);
        }

        @Override
        public String label() {
            return "-";
        }

        @Override
        public int getAppliedNumber(int base, int with) {
            return base - with;
        }
    },
    Multiply {
        // 通過すると乗算される
        @Override
        public Color backgroundColorEnd() {
            // #01f6dd
            return new Color(1, 246, 221, 240);
        }

        @Override
        public String label() {
            return "x";
        }

        @Override
        public int getAppliedNumber(int base, int with) {
            return base * with;
        }
    },
    Divide {
        // 通過すると除算される
        @Override
        public Color backgroundColorEnd() {
            return new Color(110, 86, 207, 240);
        }

        @Override
        public String label() {
            return "÷";
        }

        @Override
        public int getAppliedNumber(int base, int with) {
            if (with != 0) {
                return (int) Math.ceil((double) base / with);
            } else {
                return base;
            }
        }
    };
    // more?

    public Color backgroundColorStart() {
        Color end = backgroundColorEnd();
        return new Color(end.getRed(), end.getGreen(), end.getBlue(), 255 / 2);
    }

    public abstract Color backgroundColorEnd();

    public Color borderColor() {
        return backgroundColorEnd().darker();
    }

    public Color textColor() {
        return Color.white;
    }

    public abstract String label();

    public float textBorderWidth() {
        return 6.0f;
    }

    public Color textBorderColor() {
        return Color.black;
    }

    public abstract int getAppliedNumber(int base, int with);
}
