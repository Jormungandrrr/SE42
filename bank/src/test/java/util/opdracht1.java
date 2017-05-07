package util;

import bank.dao.AccountDAOJPAImpl;
import bank.domain.Account;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jorrit
 */
public class opdracht1 {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");
    EntityManager em;

    @Before
    public void setUp() {
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() throws SQLException {
        DatabaseCleaner.clean(em);
    }

    @Test
    public void persistCommitTest() {
        Account account = new Account(112L);
        em.getTransaction().begin();
        em.persist(account);
        /**
         * persist() maakt de instantie aan.
         */
        assertNull(account.getId());
        em.getTransaction().commit();
        System.out.println("AccountId: " + account.getId());
        /**
         * commit() probeert het naar de database te schrijven.
         */
        assertTrue(account.getId() > 0L);
    }

    @Test
    public void rollbackTest() {
        Account account = new Account(111L);
        em.getTransaction().begin();
        em.persist(account);
        assertNull(account.getId());
        em.getTransaction().rollback();
        AccountDAOJPAImpl accountDAOJPAImpl = new AccountDAOJPAImpl(em);
        List<Account> findAll = accountDAOJPAImpl.findAll();
        assertEquals(findAll.size(), 0);
        /**
         * findAll() laad hij alles wat hij kan vinden.
         */
    }

    @Test
    public void flushTest() {
        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        em.getTransaction().begin();
        em.persist(account);
        /**
         * Omdat er hier nog niet gecommit is heeft account dus ook nog geen ID
         * gekregen van de database dus blijft die gelijk aan de -100L.
         */
        assertEquals(expected, account.getId());
        em.flush();
        /**
         * Flush controleerd in de database, maar zet het er nog niet
         * daadwerkelijk in.
         */
        assertNotEquals(expected, account.getId());
        em.getTransaction().commit();
         /**
         * commit() probeert het naar de database te schrijven.
         */
    }

    @Test
    public void editAfterPersistTest() {
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();
        assertEquals(expectedBalance, account.getBalance());       
        /**
         * aanpassingen na persist() zijn doorgevoerd daarom zijn excpectedBalance en account.getBalance() gelijk
         */
        
        
        Long acId = account.getId();
        account = null;
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, acId);
        assertEquals(expectedBalance, found.getBalance());       
         /**
          * als commit correct is doorgevoerd is found.getbalance() gelijk aan expectedBalance
         */
        
    }

    @Test
    public void refreshTest() {
        Long expectedBalance = 400L;
        Long changedBalance = 200L;
        Account account = new Account(114L);
        em.getTransaction().begin();
        em.persist(account);
        account.setBalance(expectedBalance);
        em.getTransaction().commit();
        assertEquals(expectedBalance, account.getBalance());
        Long acId = account.getId();
        EntityManager em2 = emf.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, acId);
        assertEquals(expectedBalance, found.getBalance());

        account.setBalance(changedBalance);
        em.getTransaction().begin();
        em.getTransaction().commit();
        em2.refresh(found);
        assertEquals(changedBalance, found.getBalance());       
        /**
         * na een commit word de found gerefreshed en weer geupdate. 
         * daarom is ChangedBalance gelijk aan found.getBalance()
         */
    }

    @Test
    public void mergeTest() {
        Account acc = new Account(1L);
        Account acc2;

        //1
        Long balance1 = 100L;
        em.getTransaction().begin();
        em.persist(acc);
        acc.setBalance(balance1);
        em.getTransaction().commit();
        assertEquals(balance1, acc.getBalance());      
        /**
         * nog maar een account 
         */

        //2
        Long balance2a = 225L;
        Long balance2b = 235L;
        em.getTransaction().begin();
        acc2 = em.merge(acc);
        acc.setBalance(balance2a);
        acc2.setBalance(balance2b);
        em.getTransaction().commit();
        assertEquals(balance2b, acc.getBalance());
        assertEquals(balance2b, acc2.getBalance());
        /**
         * omdat acc2 gereset is word het balance van acc weer heschreven
         */


        //3
        Long balance3c = 333L;
        Long balance3d = 344L;
        em.getTransaction().begin();
        acc2 = em.merge(acc);
        assertTrue(em.contains(acc));
        assertTrue(em.contains(acc2));
        assertEquals(acc, acc2);
        acc2.setBalance(balance3c);
        acc.setBalance(balance3d);
        em.getTransaction().commit();
        assertEquals(balance3d, acc.getBalance());
        assertEquals(balance3d, acc2.getBalance());
        /**
         * word weinig anders getest dan bij 3
         */

        //4
        Account account = new Account(114L);
        account.setBalance(450L);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650l);
        assertEquals((Long) 650L, account2.getBalance());
        /**
         * linked in java
         */
        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        assertSame(account, account2);
        /**
         * accounts zijn het zelfde dus correct gesynced bij een merge
         */
        assertTrue(em.contains(account2));
        /**
         * account is al gecommit en gelijk aan account2
         * 
         */
        assertFalse(em.contains(tweedeAccountObject));
        /**
         * tweedeAccountObject niet meer gelinked omdat account2 gereset is naar de merge waarde
         */
        tweedeAccountObject.setBalance(850l);
        assertEquals((Long) 650L, account.getBalance());
        assertEquals((Long) 650L, account2.getBalance());
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void findAndClearTest() {
        Account acc1 = new Account(77L);
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();

        //1        
        Account accF1;
        Account accF2;
        accF1 = em.find(Account.class, acc1.getId());
        accF2 = em.find(Account.class, acc1.getId());
        assertSame(accF1, accF2);

        //2        
        accF1 = em.find(Account.class, acc1.getId());
        em.clear();
        accF2 = em.find(Account.class, acc1.getId());
        /**
         * clear() ruimt de locale compnenten van em op.
         * find(). zoekt gegevens op in de database
         */
    }

    @Test
    public void removeTest() {
        Account acc1 = new Account(88L);
        em.getTransaction().begin();
        em.persist(acc1);
        em.getTransaction().commit();
        Long id = acc1.getId();

        em.remove(acc1);
        assertEquals(id, acc1.getId());
        Account accFound = em.find(Account.class, id);
        assertNull(accFound);
        /**
         * assertEquals() id is geset naar de waarde van acc1.getID(). 
         * assertNull() het account is al verwijderd daarom is het null
         */
    }

    @Test
    public void generationTypeTest() {
        /**
         * In de account classe hebben we GenerationType veranderden vervolgens ge-rebuild en opnieuw getest. 
         * SEQUENCE voerde alles precies hetzelfde uit,
         * Bij TABLE faalde bijna alle tests.
         */
    }
}