package org.example.model.to;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserTO implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private Date birthday;
}
