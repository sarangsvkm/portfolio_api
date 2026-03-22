package com.sarangsvkm.portfolio_api.apiuser;

import java.util.Optional;

public interface ApiUserService {

    ApiUser register(ApiUser apiUser);

    ApiUser login(String username, String password);

    Optional<ApiUser> findById(int userId);

    void delete(int userId);
}
