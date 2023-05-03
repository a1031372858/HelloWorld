package org.example.model.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class UserPO {

    private Long id;

    private String name;

    private String mobile;

    private Date birthday;
}
