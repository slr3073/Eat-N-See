package slr.entities.Flickr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import slr.entities.Photo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoFlicker {
    private String url_l;
    private String title;
    private String ownername;

    public Photo getPhoto() {
        return new Photo(url_l, title, ownername);
    }
}
