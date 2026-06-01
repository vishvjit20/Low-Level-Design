import java.util.ArrayList;
import java.util.List;

interface IFileSystemItem {
    int getSize();
    void getDetails();
}

class File implements IFileSystemItem {

    String name;
    String content;
    public File (String name, String content) {
        this.name = name;
        this.content = content;
    }

    @Override
    public int getSize() {
        return content.length();
    }

    @Override
    public void getDetails() {
        System.out.println("File : " + name);
    }
    
}

class Folder implements IFileSystemItem {
    String name;
    String content;
    List<IFileSystemItem> children;
    public Folder (String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    void addChildren(IFileSystemItem fileSystemItem) {
        children.add(fileSystemItem);
    }

    @Override
    public int getSize() {
        int size = 0;
        for (IFileSystemItem node : children) {
            size += node.getSize();
        }
        return size;
    }

    @Override
    public void getDetails() {
        System.out.println("Inside folder: " + name);
        for (IFileSystemItem node : children) {
            node.getDetails();
        }
    }
}

public class FileSystem_Design {
    public static void main(String[] args) {
        IFileSystemItem file1 = new File("Pradeep1.txt", "lld course");
        IFileSystemItem file2 = new File("Pradeep2.txt", "hld course");


        IFileSystemItem file3 = new File("download.txt", "ldasdsadasdasdsadasdsadourse");
        IFileSystemItem file4 = new File("download2.txt", "dasdaddsadasdasddasd");

        Folder root = new Folder("root");     
        root.addChildren(file1);
        root.addChildren(file2);

        Folder download = new Folder("download");     
        download.addChildren(file3);
        download.addChildren(file4);

        root.addChildren(download);

        root.getDetails();

        System.out.println("Size" +root.getSize());

    }
}
