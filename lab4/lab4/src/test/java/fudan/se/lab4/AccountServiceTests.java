package fudan.se.lab4;

import fudan.se.lab4.constant.InfoConstant;
import fudan.se.lab4.entity.User;
import fudan.se.lab4.repository.UserRepository;
import fudan.se.lab4.service.impl.AccountServiceImpl;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountServiceTests {

    private AccountServiceImpl accountService;
    private final String START = "starbb_";
    private Mockery context;
    private UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Before
    public void setUp() {
        accountService = new AccountServiceImpl();
        context=new Mockery();
        userRepository=context.mock(UserRepository.class);
        context.checking(new Expectations(){
            {
                User user=new User();
                user.setName("starbb_hu");
                user.setPassword("huhu_123");
                User user1=new User();
                user1.setName("starbb_ss");
                user1.setPassword("ssss_12345");
                allowing(userRepository).getUser("starbb_hu");
                will(returnValue(user));
                allowing(userRepository).getUser("starbb_ss");
                will(returnValue(user1));
                allowing(userRepository).isExisted("");
                will(returnValue(false));
                allowing(userRepository).isExisted("bbbking_endhere_nonono");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_hu");
                will(returnValue(true));
                allowing(userRepository).isExisted("starbb_huhu");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_ss");
                will(returnValue(true));
                allowing(userRepository).isExisted("hu_123");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_oooot");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_$%^*9");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_909090909090909909090909090909909090909090909909090909090909909090909090909909090909090909");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_xca");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_ccdd");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_ssijiji");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_srxsrx");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_caca");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_ninnu");
                will(returnValue(false));
                allowing(userRepository).isExisted("starbb_huhuxaxa");
                will(returnValue(false));

                allowing(userRepository).createUser(with(aNonNull(User.class)));

            }
        });
        accountService.setUserRepository(userRepository);

    }

    @After
    public void tearDown() {
        accountService = null;
    }

    //测试登陆：传入参数为null
    @Test
    public void testLoginUserNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        accountService.login(null);
    }
    //测试登陆：user不为null，但是name为null,密码不为null
    @Test
    public void testLoginUserNameNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        User user=new User();
        user.setName(null);
        user.setPassword("123");
        accountService.login(user);
    }
    //测试登陆：user不为null，但是name为null,密码为null
    @Test
    public void testLoginUserNamePwdBothNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        User user=new User();
        user.setName(null);
        user.setPassword(null);
        accountService.login(user);
    }
    //测试登陆：user不为null，但是密码为null,name不为null
    @Test
    public void testLoginUserPwdNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        User user=new User();
        user.setName("starbb_huhu");
        user.setPassword(null);
        accountService.login(user);
    }
    //测试登陆：user的名字为空，密码也为空
    @Test
    public void testLoginUserNamePwdEmpty() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.USER_NOT_FOUND);
        User user=new User();
        user.setName("");
        user.setPassword("");
        accountService.login(user);
    }
    //测试登陆：uesr的名字为空，密码非空
    @Test
    public void testLoginUserNameEmpty() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.USER_NOT_FOUND);
        User user=new User();
        user.setName("");
        user.setPassword("13489");
        accountService.login(user);
    }

    //测试登陆：user的名字和密码都非空
    //测试登陆：用户名不存在
    @Test
    public void testLoginUserNotExisted() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(MessageFormat.format(InfoConstant.USER_NOT_FOUND,"bbbking_endhere_nonono"));
        User user=new User();
        user.setName("bbbking_endhere_nonono");
        user.setPassword("123456aa_6");
        accountService.login(user);
    }
    //测试登陆：用户名存在但是密码不正确
    @Test
    public void testLoginUserPwdFalse() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.WRONG_MATCH);
        User user=new User();
        user.setName("starbb_hu");
        user.setPassword("1234566");
        accountService.login(user);
    }
    //测试登陆：用户名存在，密码内容正确，但前后多了空格
    @Test
    public void testLoginUserBlank() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.WRONG_MATCH);
        User user=new User();
        user.setName("starbb_hu");
        user.setPassword("   huhu_123");
        accountService.login(user);

    }
    //测试登陆：用户存在，密码也正确
    @Test
    public void testLoginUser() {

        //初始化一些变量，方便测试
        User user=new User();
        boolean flag;
        //传入对象名字和密码都正确
        user.setName("starbb_hu");
        user.setPassword("huhu_123");
        flag = accountService.login(user);
        assertTrue(flag);
        //传入对象名字和密码都正确
        user.setName("starbb_ss");
        user.setPassword("ssss_12345");
        flag = accountService.login(user);
        assertTrue(flag);
//        //传入对象名字和密码都正确
//        user.setName("starbb_srx");
//        user.setPassword("srx_12345");
//        flag = accountService.login(user);
//        assertTrue(flag);
    }

    //测试注册：传入参数为null
    @Test
    public void testSignUpUserNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        accountService.signup(null);
    }
    //测试注册：user不为null，但是name为null,密码不为null
    @Test
    public void testSignUpUserNameNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        User user=new User();
        user.setName(null);
        user.setPassword("123aaaaa_");
        accountService.signup(user);
    }
    //测试注册：user不为null，但是name为null,密码为null
    @Test
    public void testSignUpUserNamePwdBothNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        User user=new User();
        user.setName(null);
        user.setPassword(null);
        accountService.signup(user);
    }
    //测试注册：user不为null，但是密码为null,name不为null
    @Test
    public void testSignUpnUserPwdNull() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.NULL_POINTER);
        User user=new User();
        user.setName("starbb_oooo");
        user.setPassword(null);
        accountService.signup(user);
    }

    //测试注册：user不为null，密码和name都不为null
    //测试注册：user的名字为空，密码也为空
    @Test
    public void testSignUpUserNamePwdEmpty() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_USERNAME);
        User user=new User();
        user.setName("");
        user.setPassword("");
        accountService.signup(user);
    }
    //测试注册：uesr的名字为空，密码非空
    @Test
    public void testSignUpUserNameEmpty() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_USERNAME);
        User user=new User();
        user.setName("");
        user.setPassword("aaaa1111_");
        accountService.signup(user);
    }
    //测试注册：user的名字非空，密码为空
    @Test
    public void testSignUpUserPwdEmpty() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user=new User();
        user.setName("starbb_oooot");
        user.setPassword("");
        accountService.signup(user);
    }
    //测试注册：user的名字和密码都非空
    //测试注册：用户名已存在
    @Test
    public void testSignUpUserExisted() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(MessageFormat.format(InfoConstant.ENTITY_EXIST,"User","starbb_hu"));
        User user=new User();
        user.setName("starbb_hu");
        user.setPassword("aaaa1111__");
        accountService.signup(user);
    }
    //测试注册：用户名不是已存在，但是用户名不符合规范，密码符合规范
    //不以starbb_开头
    @Test
    public void testSignUpUserNameFalse1() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_USERNAME);
        User user=new User();
        user.setName("hu_123");
        user.setPassword("aa_12345");
        accountService.signup(user);

    }
    //非法字符
    @Test
    public void testSignUpUserNameFalse2() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_USERNAME);
        User user = new User();
        user.setName("starbb_$%^*9");
        user.setPassword("bb_7890909");
        accountService.signup(user);
    }
    //长度小于8
    @Test
    public void testSignUpUserNameFalse3() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_USERNAME);
        User user = new User();
        user.setName("starbb_");
        user.setPassword("cc_78390909");
        accountService.signup(user);
    }
    //长度大于50
    @Test
    public void testSignUpUserNameFalse4() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_USERNAME);
        User user = new User();
        user.setName("starbb_909090909090909909090909090909909090909090909909090909090909909090909090909909090909090909");
        user.setPassword("dd_78909909090");
        accountService.signup(user);
    }


    //测试注册：用户名不是已存在且用户名符合规范，但是密码不符合规范
    //缺少必须成分
    @Test
    public void testSignUpUserPwdWrong1() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user=new User();
        user.setName("starbb_xca");
        user.setPassword("huhu787823");
        accountService.signup(user);
    }
    //缺少必须成分
    @Test
    public void testSignUpUserPwdWrong2() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user = new User();
        user.setName("starbb_xca");
        user.setPassword("____787823");
        accountService.signup(user);
    }
    //缺少必须成分
    @Test
    public void testSignUpUserPwdWrong3() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user = new User();
        user.setName("starbb_xca");
        user.setPassword("huhu____");
        accountService.signup(user);
    }
    //存在非法字符
    @Test
    public void testSignUpUserPwdWrong4() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user = new User();
        user.setName("starbb_caca");
        user.setPassword("huhu_123#!&");
        accountService.signup(user);
    }
    //长度小于8
    @Test
    public void testSignUpUserPwdWrong5() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user = new User();
        user.setName("starbb_ninnu");
        user.setPassword("h_123");
        accountService.signup(user);
    }
    //长度大于100
    @Test
    public void testSignUpUserPwdWrong6() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(InfoConstant.INVALID_PASSWORD);
        User user = new User();
        user.setName("starbb_huhuxaxa");
        user.setPassword("h_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioioh_xx1hhhhioioioio");
        accountService.signup(user);
    }

    //测试注册：用户不存在，用户名和密码也符合规范
    @Test
    public void testSignUpUser() {
        //初始化一些变量，方便测试
        User user=new User();
        boolean flag;
        //传入对象名字和密码都正确
        user.setName("starbb_ccdd");
        user.setPassword("huhu_12345544");
        flag = accountService.signup(user);
        assertTrue(flag);
        //传入对象名字和密码都正确
        user.setName("starbb_ssijiji");
        user.setPassword("ssss_123457878");
        flag = accountService.signup(user);
        assertTrue(flag);
        //传入对象名字和密码都正确
        user.setName("starbb_srxsrx");
        user.setPassword("srx_1234566");
        flag = accountService.signup(user);
        assertTrue(flag);
    }

    //测试对登陆状态的检查
    @Test
    public void testCheckStatusLogedIn(){
//        AccountServiceImpl accountService1 = new AccountServiceImpl();
        User user = new User();
        user.setName("starbb_hu");
        user.setPassword("huhu_123");
        accountService.login(user);
        assertTrue(accountService.checkStatus());
    }
    @Test
    public void testCheckStatusNotLogedIn(){
//        AccountServiceImpl accountService1 = new AccountServiceImpl();
        assertFalse(accountService.checkStatus());
    }

    //测试检查用户名接口
    @Test
    public void testCheckNameNull(){
        boolean flag = accountService.checkName(null);
        assertFalse(flag);
    }
    @Test
    public void testCheckNameStart() {
        //有一个starbb_在开头,合法的情况
        boolean flag = accountService.checkName(START + "_9890");
        assertTrue(flag);
        //有且只有两个starbb_
        flag = accountService.checkName(START + START);
        assertTrue(flag);
        //starbb_在中间
        flag = accountService.checkName("_9080" + START + "0");
        assertFalse(flag);
        //starbb_在最后
        flag = accountService.checkName("asd_" + START);
        assertFalse(flag);
        //没有starbb_
        flag = accountService.checkName("asd_sdasd234" );
        assertFalse(flag);
        //只有starbb,没有下划线（即只有一部分）
        flag = accountService.checkName("starbbasd_sdasd234" );
        assertFalse(flag);
        //前缀大写
        flag = accountService.checkName("STARBB_asd_sdasd234" );
        assertFalse(flag);
    }

    @Test
    public void testCheckNameInvalidLetter() {
        //用户名中存在小数点
        boolean flag = accountService.checkName(START + "1234567.");
        assertFalse(flag);
        //用户名中存在换行转义字符
        flag = accountService.checkName(START + "1234567\n");
        assertFalse(flag);
        //用户名中存在逻辑判断语句，该检测是为了防止未来使用数据库的话，防止SQL注入
        flag = accountService.checkName(START + "123or'1'=='1'");
        assertFalse(flag);
        flag = accountService.checkName(START+"123||'1'=='1'");
        assertFalse(flag);
        //用户名中存在@符号
        flag = accountService.checkName(START + "@12334442");
        assertFalse(flag);
        //用户名中存在空格
        flag = accountService.checkName(START+"    dsa");
        assertFalse(flag);
        //用户名中存在表示字符串结束的转义字符'\0'
        flag = accountService.checkName(START+"huuhuh\0");
        assertFalse(flag);
        //正确的用户名表示测试
        flag = accountService.checkName(START + "123_23daswHHGd");
        assertTrue(flag);
        //除必要的前缀外其余都是数字
        flag = accountService.checkName(START + "12300");
        assertTrue(flag);
        //除必要的前缀之外其余全是下划线
        flag = accountService.checkName(START + "______");
        assertTrue(flag);
        //用户名中只有字母，大小写字母都有
        flag = accountService.checkName(START + "kloPP");
        assertTrue(flag);
        //用户名中除必须的前缀之外全是大写字母
        flag = accountService.checkName(START+"HIIHIHIHII");
        assertTrue(flag);
        //用户名中除必须的前缀之外全是小写字母
        flag = accountService.checkName(START+"kkkkllklk");
        assertTrue(flag);
    }

    @Test
    public void testCheckNameLength() {
        //长度为0
        boolean flag = accountService.checkName("");
        assertFalse(flag);
        //长度为7
        flag = accountService.checkName(START + "1234567");
        assertTrue(flag);
        //长度为8
        flag = accountService.checkName(START + "1");
        assertTrue(flag);
        //长度在范围中
        flag = accountService.checkName(START + "12345");
        assertTrue(flag);
        //长度为49
        flag = accountService.checkName(START + "123456789012345678901234567890123456789012");
        assertTrue(flag);
        //长度为50
        flag = accountService.checkName(START + "1234567890123456789012345678901234567890123");
        assertFalse(flag);
        //长度大于50
        flag = accountService.checkName(START + "12345678901234567890123456789012345678901231345325");
        assertFalse(flag);
    }

    //测试检查密码接口
    @Test
    public void testPasswordNull(){
        boolean flag = accountService.checkPassword(null);
        assertFalse(flag);
    }

    @Test
    public void testPasswordInvalidLetter(){
        //传入正确密码
        boolean flag = accountService.checkPassword("aaaa111_");
        assertTrue(flag);
        //传入不包含合法字符的密码
        flag = accountService.checkPassword(")(%^&$##@");
        assertFalse(flag);
        //传入既包含合法又包含非法的密码
        flag = accountService.checkPassword("?aaaa 111_*");
        assertFalse(flag);
    }

    @Test
    public void testPasswordConstitute(){
        //只包括字母，数字
        boolean flag = accountService.checkPassword("aaaa1111");
        assertFalse(flag);
        //只包含字母，下划线
        flag = accountService.checkPassword("aaaaaaaa_");
        assertFalse(flag);
        //只包含数字，下划线
        flag = accountService.checkPassword("12345678_");
        assertFalse(flag);
        //只包含数字
        flag = accountService.checkPassword("12345678");
        assertFalse(flag);
        //只包含字母
        flag = accountService.checkPassword("aaaaaaaa");
        assertFalse(flag);
        //只包含下划线
        flag = accountService.checkPassword("__________");
        assertFalse(flag);
    }

    @Test
    public void testPasswordLength(){
        //长度为0
        boolean flag = accountService.checkPassword("");
        assertFalse(flag);
        //长度为3
        flag = accountService.checkPassword("a1_");
        assertFalse(flag);
        //长度为8
        flag = accountService.checkPassword("aaaa111_");
        assertTrue(flag);
        //长度为10,在范围中
        flag = accountService.checkPassword("aaaaa1111_");
        assertTrue(flag);
        //长度为100
        flag = accountService.checkPassword("11111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa_");
        assertFalse(flag);
        //长度为101
        flag = accountService.checkPassword("111111111111111111111111111111111111111111111111111aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa_");
        assertFalse(flag);
    }


}
