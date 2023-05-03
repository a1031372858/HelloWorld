package org.example.model.to;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class UserTO {

    private Long id;

    private String name;

    private String mobile;

    private Date birthday;
}
