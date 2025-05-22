import java.awt.Image;

public class Pipe extends GameObject {
    private boolean passed = false;

    public Pipe(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

}
