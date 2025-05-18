import java.awt.Image;

public class Pipe extends GameObject {
    public boolean passed = false;

    public Pipe(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }

}
