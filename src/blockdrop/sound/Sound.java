package blockdrop.sound;

import blockdrop.BlockDrop;

import java.net.URL;

public abstract class Sound {
    private final String path;
    private final URL resource;

    public Sound(String path) {
        this.path = path;
        resource = BlockDrop.class.getResource(String.format("../sounds/%s", path));
    }

    public URL getResource() {
        return resource;
    }

    public String getPath() {
        return resource.toString();
    }

    public abstract void setUp();
    public abstract void play();
}
