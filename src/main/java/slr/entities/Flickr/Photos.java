package slr.entities.Flickr;

import lombok.Data;

import java.util.List;

@Data
public class Photos {
    private int page;
    private int pages;
    private int total;
    List<PhotoFlicker> photo;
}
