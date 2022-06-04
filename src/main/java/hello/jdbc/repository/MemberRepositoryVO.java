package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

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
            ps.executeUpdate(); //실행시키기
            return member;

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(connection, ps, null);
        }
    }


    /**
     * id로 조회하기
     */
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{

            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, memberId);
            rs = ps.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId="+ memberId);
            }

        } catch (SQLException e) {
           log.info("error", e);
           throw e;

        } finally {
            close(connection, ps, rs);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setInt(1, money);
            ps.setString(2, memberId);

            int resultSize = ps.executeUpdate();
            log.info("resultSize={}", resultSize);


        } catch (SQLException e) {
            log.error("error", e);
            throw e;
        } finally {
            close(connection, ps, rs);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;


        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, memberId);

            ps.executeUpdate();


        } catch (SQLException e) {
            log.error("error", e);
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
