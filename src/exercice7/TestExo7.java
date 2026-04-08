package exercice7;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import exercice7.*;
import stree.parser.SNode;
import stree.parser.SParser;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests JUnit pour l'exercice 7.
 *
 * 
 */
class Exercice7Test {


    //  parse une s-expression en SNode
    private SNode parse(String src) {
        try {
            SParser<SNode> parser = new SParser<>();
            List<SNode> nodes = parser.parse(src);
            return nodes.get(0);
        } catch (IOException e) {
            throw new RuntimeException("Erreur de parsing : " + src, e);
        }
    }

    /** Construit un environnement standard avec toutes les commandes de l'exercice 7. */
    private Environment buildEnv() {
        Environment env = new Environment();
        env.addReference("+",  new Reference(new AddCommand()));
        env.addReference("-",  new Reference(new SubCommand()));
        env.addReference("*",  new Reference(new MulCommand()));
        env.addReference("/",  new Reference(new DivCommand()));
        env.addReference(">",  new Reference(new StricSupCommand()));
        env.addReference("<",  new Reference(new StricInfCommand()));
        env.addReference(">=", new Reference(new SupCommand()));
        env.addReference("<=", new Reference(new InfCommand()));
        env.addReference("=",  new Reference(new EgalCommand()));
        return env;
    }


    //  1. Tests de Reference

    @Nested
    @DisplayName("Reference")
    class ReferenceTests {

        @Test
        @DisplayName("asInt() depuis un Integer")
        void asIntFromInteger() {
            Reference r = new Reference(42);
            assertEquals(42, r.asInt());
        }

        @Test
        @DisplayName("asInt() depuis une String numérique")
        void asIntFromString() {
            Reference r = new Reference("17");
            assertEquals(17, r.asInt());
        }

        @Test
        @DisplayName("asInt() depuis une String non-numérique  exception")
        void asIntFromNonNumericString() {
            Reference r = new Reference("abc");
            assertThrows(RuntimeException.class, r::asInt);
        }

        @Test
        @DisplayName("isTrue() vrai pour Boolean true")
        void isTrueForTrue() {
            assertTrue(new Reference(true).isTrue());
        }

        @Test
        @DisplayName("isTrue() faux pour Boolean false")
        void isTrueForFalse() {
            assertFalse(new Reference(false).isTrue());
        }

        @Test
        @DisplayName("isTrue() vrai pour Integer non-nul")
        void isTrueForNonZeroInt() {
            assertTrue(new Reference(1).isTrue());
        }

        @Test
        @DisplayName("isTrue() faux pour Integer 0")
        void isTrueForZeroInt() {
            assertFalse(new Reference(0).isTrue());
        }

        @Test
        @DisplayName("isTrue() faux pour String vide")
        void isTrueForEmptyString() {
            assertFalse(new Reference("").isTrue());
        }

        @Test
        @DisplayName("isTrue() vrai pour String non-vide")
        void isTrueForNonEmptyString() {
            assertTrue(new Reference("hello").isTrue());
        }

        @Test
        @DisplayName("getReceiver() retourne l'objet encapsulé")
        void getReceiverReturnsObject() {
            Object obj = new Object();
            Reference r = new Reference(obj);
            assertSame(obj, r.getReceiver());
        }

        @Test
        @DisplayName("setReceiver() modifie le receiver")
        void setReceiverUpdatesValue() {
            Reference r = new Reference(1);
            r.setReceiver(99);
            assertEquals(99, r.getReceiver());
        }
    }


    //  2. Tests de Environment

    @Nested
    @DisplayName("Environment")
    class EnvironmentTests {

        @Test
        @DisplayName("addReference puis getReferenceByName retrouve la valeur")
        void addAndGet() {
            Environment env = new Environment();
            Reference r = new Reference(10);
            env.addReference("x", r);
            assertSame(r, env.getReferenceByName("x"));
        }

        @Test
        @DisplayName("getReferenceByName retourne null si absent (sans parent)")
        void getUnknownReturnsNull() {
            Environment env = new Environment();
            assertNull(env.getReferenceByName("inconnu"));
        }

        @Test
        @DisplayName("Environnement enfant délègue au parent si clé absente")
        void childFallsBackToParent() {
            Environment parent = new Environment();
            Reference r = new Reference(5);
            parent.addReference("y", r);

            Environment child = new Environment(parent);
            assertSame(r, child.getReferenceByName("y"));
        }

        @Test
        @DisplayName("Environnement enfant peut masquer une variable du parent")
        void childShadowsParent() {
            Environment parent = new Environment();
            parent.addReference("z", new Reference(1));

            Environment child = new Environment(parent);
            Reference local = new Reference(99);
            child.addReference("z", local);

            assertSame(local, child.getReferenceByName("z"));
        }
    }


    //  3. Tests de l'Interpreter


    @Nested
    @DisplayName("Interpreter")
    class InterpreterTests {

        @Test
        @DisplayName("compute() sur une feuille connue retourne la référence")
        void computeLeafKnown() {
            Environment env = new Environment();
            Reference r = new Reference(7);
            env.addReference("x", r);

            SNode leaf = parse("x"); // feuille
            // On parse juste "x" qui est une feuille
            // En fait parse("x") va créer un SNode feuille directement si on le traite bien
            Reference result = new Interpreter().compute(env, parse("x"));
            // Si x est une feuille, l'interpréteur retourne la référence
            // si parse("x") retourne un nœud feuille, c'est bon
            assertNotNull(result);
        }

        @Test
        @DisplayName("compute() d'une addition simple : (+ 3 4) = 7")
        void computeSimpleAdd() {
            Environment env = buildEnv();
            Reference result = new Interpreter().compute(env, parse("(+ 3 4)"));
            assertEquals(7, result.asInt());
        }

        @Test
        @DisplayName("compute() imbriqué : (+ (* 2 3) 1) = 7")
        void computeNested() {
            Environment env = buildEnv();
            Reference result = new Interpreter().compute(env, parse("(+ (* 2 3) 1)"));
            assertEquals(7, result.asInt());
        }
    }


    //  4. Tests arithmétiques via Interpreter


    @Nested
    @DisplayName("Commandes arithmétiques")
    class ArithmeticTests {

        private Environment env;

        @BeforeEach
        void setUp() { env = buildEnv(); }

        @ParameterizedTest(name = "(+ {0} {1}) = {2}")
        @CsvSource({"3,4,7", "0,0,0", "-5,5,0", "100,200,300"})
        @DisplayName("Addition")
        void addition(int a, int b, int expected) {
            String expr = String.format("(+ %d %d)", a, b);
            assertEquals(expected, new Interpreter().compute(env, parse(expr)).asInt());
        }

        @ParameterizedTest(name = "(- {0} {1}) = {2}")
        @CsvSource({"10,3,7", "0,0,0", "5,10,-5"})
        @DisplayName("Soustraction")
        void subtraction(int a, int b, int expected) {
            String expr = String.format("(- %d %d)", a, b);
            assertEquals(expected, new Interpreter().compute(env, parse(expr)).asInt());
        }

        @ParameterizedTest(name = "(* {0} {1}) = {2}")
        @CsvSource({"3,4,12", "0,99,0", "-2,5,-10"})
        @DisplayName("Multiplication")
        void multiplication(int a, int b, int expected) {
            String expr = String.format("(* %d %d)", a, b);
            assertEquals(expected, new Interpreter().compute(env, parse(expr)).asInt());
        }

        @ParameterizedTest(name = "(/ {0} {1}) = {2}")
        @CsvSource({"10,2,5", "9,3,3", "7,2,3"})
        @DisplayName("Division entière")
        void division(int a, int b, int expected) {
            String expr = String.format("(/ %d %d)", a, b);
            assertEquals(expected, new Interpreter().compute(env, parse(expr)).asInt());
        }

        @Test
        @DisplayName("Division par zéro lance une exception")
        void divisionByZero() {
            assertThrows(Exception.class, () ->
                new Interpreter().compute(env, parse("(/ 5 0)"))
            );
        }
    }


    //  5. Tests de comparaison


    @Nested
    @DisplayName("Commandes de comparaison")
    class ComparisonTests {

        private Environment env;

        @BeforeEach
        void setUp() { env = buildEnv(); }

        // ── < (StricInfCommand) ────────────────────────
        @Test @DisplayName("(< 3 5) = true")
        void infTrue()  { assertTrue(eval("(< 3 5)")); }

        @Test @DisplayName("(< 5 3) = false")
        void infFalse() { assertFalse(eval("(< 5 3)")); }

        @Test @DisplayName("(< 4 4) = false  (strict)")
        void infEqual() { assertFalse(eval("(< 4 4)")); }

        // ── > (StricSupCommand) ────────────────────────
        @Test @DisplayName("(> 5 3) = true")
        void supTrue()  { assertTrue(eval("(> 5 3)")); }

        @Test @DisplayName("(> 3 5) = false")
        void supFalse() { assertFalse(eval("(> 3 5)")); }

        @Test @DisplayName("(> 4 4) = false  (strict)")
        void supEqual() { assertFalse(eval("(> 4 4)")); }

        // ── <= (InfCommand) ──────────────────
        @Test @DisplayName("(<= 3 5) = true")
        void stricInfLess()  { assertTrue(eval("(<= 3 5)")); }

        @Test @DisplayName("(<= 4 4) = true  (égalité incluse)")
        void stricInfEqual() { assertTrue(eval("(<= 4 4)")); }

        @Test @DisplayName("(<= 5 3) = false")
        void stricInfMore()  { assertFalse(eval("(<= 5 3)")); }

        // ── >= (SupCommand) ──────────────────
        @Test @DisplayName("(>= 5 3) = true")
        void stricSupMore()  { assertTrue(eval("(>= 5 3)")); }

        @Test @DisplayName("(>= 4 4) = true  (égalité incluse)")
        void stricSupEqual() { assertTrue(eval("(>= 4 4)")); }

        @Test @DisplayName("(>= 3 5) = false")
        void stricSupLess()  { assertFalse(eval("(>= 3 5)")); }

        // ── = (EgalCommand) ───────────────────────
        @Test @DisplayName("(= 4 4) = true")
        void egalTrue()  { assertTrue(eval("(= 4 4)")); }

        @Test @DisplayName("(= 3 4) = false")
        void egalFalse() { assertFalse(eval("(= 3 4)")); }

        // ── Comparaisons imbriquées ───────────────
        @Test @DisplayName("(< (+ 1 2) (* 2 3)) = true  (3 < 6)")
        void nestedComparison() {
            assertTrue(eval("(< (+ 1 2) (* 2 3))"));
        }

        /** Évalue l'expression et retourne le booléen résultant. */
        private boolean eval(String expr) {
            return new Interpreter().compute(env, parse(expr)).isTrue();
        }
    }


    //  6. Tests des commandes directement


    @Nested
    @DisplayName("Commandes unitaires (sans Interpreter)")
    class DirectCommandTests {

        private Environment env;

        @BeforeEach
        void setUp() { env = buildEnv(); }

        @Test
        @DisplayName("InfCommand.run() : 2 < 8 = true")
        void infCommandDirect() {
            Reference self = env.getReferenceByName("<");
            SNode method = parse("(< 2 8)");
            Reference result = new InfCommand().run(self, method, env);
            assertTrue((Boolean) result.getReceiver());
        }

        @Test
        @DisplayName("SupCommand.run() : 8 > 2 = true")
        void supCommandDirect() {
            Reference self = env.getReferenceByName(">");
            SNode method = parse("(> 8 2)");
            Reference result = new SupCommand().run(self, method, env);
            assertTrue((Boolean) result.getReceiver());
        }

        @Test
        @DisplayName("StricInfCommand.run() : 5 <= 5 = true")
        void stricInfCommandDirect() {
            Reference self = env.getReferenceByName("<=");
            SNode method = parse("(<= 5 5)");
            Reference result = new StricInfCommand().run(self, method, env);
            assertTrue((Boolean) result.getReceiver());
        }

        @Test
        @DisplayName("StricSupCommand.run() : 5 >= 5 = true")
        void stricSupCommandDirect() {
            Reference self = env.getReferenceByName(">=");
            SNode method = parse("(>= 5 5)");
            Reference result = new StricSupCommand().run(self, method, env);
            assertTrue((Boolean) result.getReceiver());
        }

        @Test
        @DisplayName("SubCommand.run() : 10 - 3 = 7")
        void subCommandDirect() {
            Reference self = env.getReferenceByName("-");
            SNode method = parse("(- 10 3)");
            Reference result = new SubCommand().run(self, method, env);
            assertEquals(7, result.asInt());
        }
    }
}