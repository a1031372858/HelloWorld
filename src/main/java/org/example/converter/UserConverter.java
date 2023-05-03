package org.example.converter;

import org.example.base.BaseConverter;
import org.example.model.po.UserPO;
import org.example.model.to.UserTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserConverter extends BaseConverter<UserTO, UserPO> {

}
