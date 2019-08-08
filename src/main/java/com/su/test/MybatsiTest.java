package com.su.test;

import com.su.mapper.OrderMapper;
import com.su.mapper.UserMapper;
import com.su.pojo.Order;
import com.su.pojo.QueryVo;
import com.su.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MybatsiTest
{
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception
    {
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        //不需要关闭,build的时候mybatis帮助关闭
        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory = builder.build(is);
    }

    @Test
    public void testQueryUsrById()
    {
        //sqlSession里面配置,是否自动提交,执行器,游标,dirty
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = sqlSession.selectOne("queryUserById", 1);
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void testQueryVo()
    {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        QueryVo queryVo = new QueryVo();
        User user = new User();
        user.setUsername("张");
        queryVo.setUser(user);
        List<User> users = mapper.queryUserByQueryVo(queryVo);
        for (User user1 : users)
        {
            System.out.println(user1);
        }
        sqlSession.close();
    }

    @Test
    public void testQueryUser()
    {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setSex("男");
        List<User> users = mapper.queryUser(user);
        for (User user1 : users)
        {
            System.out.println(user1);
        }
        sqlSession.close();
    }

    @Test
    public void testQueryUserCount()
    {
        // mybatis和spring整合，整合之后，交给spring管理
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        // 创建Mapper接口的动态代理对象，整合之后，交给spring管理
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        // 使用userMapper执行查询用户数据条数
        int count = userMapper.queryCount();
        System.out.println(count);

        // mybatis和spring整合，整合之后，交给spring管理
        sqlSession.close();
    }

    @Test
    public void testQueryAll() throws IOException, FileNotFoundException
    {
        // 获取sqlSession
        SqlSession sqlSession = this.sqlSessionFactory.openSession();
        // 获取OrderMapper
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        // 执行查询
        List<Order> list = orderMapper.queryOrderUser();
        for (Order order : list)
        {
            System.out.println(order);
        }
        Document doc = null;
        List<Document> docs = new ArrayList<>();
        for (Order order : list)
        {
            doc = new Document();
            //是否分词,是否创建索引,是否存储
            TextField id = new TextField("id", "" + order.getId(), Field.Store.YES);
            TextField name = new TextField("id", "sssdd", Field.Store.YES);
            TextField number = new TextField("number", order.getNumber(), Field.Store.YES);
            doc.add(id);
            doc.add(name);
            doc.add(number);
            docs.add(doc);
        }

        //创建indexWriter
        FSDirectory directory = FSDirectory.open(new File("E:\\index01"));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig writerConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, writerConfig);
        for (Document document : docs)
        {
            indexWriter.addDocument(document);
        }
        indexWriter.close();

    }

    @Test
    public void doIndexSearch() throws IOException
    {
        FSDirectory directory = FSDirectory.open(new File("E:\\index01"));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        //指定查询的域和关键字
        TermQuery termQuery = new TermQuery(new Term("name", "lucene"));
        TopDocs topDocs = searcher.search(termQuery, 10);
        //遍历结果并输出
        System.out.println(">>>总记录数" + topDocs.totalHits);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs)
        {
            int doc = scoreDoc.doc;
            Document doc1 = searcher.doc(doc);
            System.out.println(doc1);
        }
        reader.close();
    }

    @Test
    public void testAnalyzer() throws IOException
    {
        //1.创建analyzer (ik)
        Analyzer analyzer = new IKAnalyzer();
        //2.获取tokenstream  (分的词都在此对象中)
        //第一个参数：就是域的名称，可以不写或者""
        //第二个参数：分析的词内容
        TokenStream tokenStream = analyzer.tokenStream("", "高富帅this is a test");//高富帅是假的
        //3.指定一个引用   （指定 词的引用   或者 偏移量）
        CharTermAttribute addAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //4.设置一个偏移量的引用
        OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        //5.调用tokenstream的rest方法 重置
        tokenStream.reset();
        //6.通过wihle 循环 遍历单词列表
        while (tokenStream.incrementToken())
        {
            ///打印
            System.out.println("start>>" + offsetAttribute.startOffset());
            System.out.println(addAttribute.toString());//打印单词
            System.out.println("end>>" + offsetAttribute.endOffset());
        }
        tokenStream.close();
        //关闭流
    }

}
