package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * jdbc drivermanager 사용
 */
@Slf4j
public class MemberRepositoryVO {

    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id,money) values(?,?)";

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, member.getMemberId());
            ps.setInt(2,member.getMoney());
            return member;

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(connection, ps, null);
        }
    }

    private void close(Connection connection, Statement statement, ResultSet resultSet){

        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }


    }

    private Connection getConnection(){
        return DBConnectionUtil.getConnection();
    }
}
