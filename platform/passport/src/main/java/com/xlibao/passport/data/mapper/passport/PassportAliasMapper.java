package com.xlibao.passport.data.mapper.passport;

import com.xlibao.passport.data.model.PassportAlias;

public interface PassportAliasMapper {

    int createPassportAlias(PassportAlias passportAlias);

    PassportAlias getPassportAlias(String name);
}