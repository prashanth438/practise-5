package 878.core.module.user.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import 878.core.module.user.service.UserService;
import 878.db.entity.User;
import 878.db.entity.property.UserProperties;
import 878.db.mapper.UserMapper;

/**
 * 用户service实现
 *
 * @author IBIT程序猿
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;


    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Override
    public User getByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return mapper
                .createQuery()
                .columnDefaultPo()
                .fromDefault()
                .andWhere(UserProperties.userName.eq(username))
                .limit(1)
                .executeQueryOne();
    }

}
