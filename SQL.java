import java.util.Scanner;
import java.sql.*;

/* 
 * @Author Ville Selkämaa 
 * @version 08.02.2022
 * */

public class SQL {

	private static final String KURSSITDATABASE = "jdbc:sqlite:kurssit.db";
	private static final String VIRHE = "Tietojen haku ei onnistunut!";
	private static final String VIRHEDEV = "SQL:ssä ongelma, palauta virheilmoitus kehittäjille.";

	public static void main(String[] args) throws SQLException {

		String intro = """
				1: Valitse vuosi, mistä etsitään opintopisteiden kokonaismäärä
				2: Kirjoita opiskelijan nimi kenen opintosuoritukset tulostetaan
				3: Syötä kurssin nimi mistä näytetään keskiarvo
				4: Opettajien määrä, ketkä ovat antaneet eniten opintopisteitä"
				5: Lopeta ohjelman suoritus
				     """;

		System.out.println(intro);

		Scanner syote = new Scanner(System.in);
		kysymys(syote);
	}

	/**
	 * Aliohjelma, jolla valitaan suoritettava toiminto
	 */
	public static void suoritaAliohjelma(String vastaus, Scanner syote) throws SQLException {
		boolean numero = true;

		numero = vastaus.matches("[0-9]+");

		if (numero) {
			int aliohjelma = Integer.parseInt(vastaus);
			switch (aliohjelma) {
			case 1:
				opintoSuorituksetVuosi(syote);
				break;
			case 2:
				suorituksetOpiskelija(syote);
				break;
			case 3:
				kurssiKeskiarvo(syote);
				break;
			case 4:
				opettajatOpintopisteet(syote);
				break;
			case 5:
				System.out.println("Ohjelma suljettu!");
				break;
			default:
				System.out.println("Virhe! Valitse väliltä 1-5");
				String virhe = syote.nextLine();
				suoritaAliohjelma(virhe, syote);
			}

		} else {
			System.out.println(vastaus + " ei ole numero");
			kysymys(syote);
		}
		syote.close();
	}

	/**
	 * Kysymys aliohjelma
	 */
	public static void kysymys(Scanner syote) throws SQLException {
		System.out.println("Valitse toimintojen 1-5 väliltä");
		String vastaus = syote.nextLine();
		suoritaAliohjelma(vastaus, syote);
	}

	/**
	 * Aliohjelma, jossa haetaan opettajat, jotka ovat jakaneet eniten
	 * opintosuorituksia
	 */
	public static void opettajatOpintopisteet(Scanner syote) throws SQLException {
		System.out.println("Anna opettajien määrä: ");
		String vastaus = syote.nextLine();

		try (Connection db = DriverManager.getConnection(KURSSITDATABASE); Statement s = db.createStatement();) {
			ResultSet r = s.executeQuery("SELECT o.nimi, SUM(k.laajuus) FROM Opettajat o, Kurssit k, Suoritukset s\r\n"
					+ "WHERE o.id = k.opettaja_id AND s.kurssi_id=k.id\r\n" + "GROUP BY o.nimi\r\n"
					+ "ORDER BY SUM(k.laajuus) DESC LIMIT " + vastaus);

			if (r.next()) {
				System.out.println(r.getString("nimi") + " | " + r.getInt("SUM(k.laajuus)"));
				while (r.next()) {
					System.out.println(r.getString("nimi") + " | " + r.getInt("SUM(k.laajuus)"));
				}
			} else {
				System.out.println(VIRHE);
				kysymys(syote);
			}
		} catch (SQLException e) {
			System.out.println(VIRHE);
		}
		kysymys(syote);
	}

	/**
	 * Aliohjelma, missä lasketaan opintopisteet vuodelta, jonka käyttäjä syötti
	 */
	private static void opintoSuorituksetVuosi(Scanner syote) throws SQLException {
		System.out.println("Valitse vuosi, mistä tulostetaan opintopisteiden kokonaismäärä: ");
		String syotetty = syote.nextLine();

		try (Connection db = DriverManager.getConnection(KURSSITDATABASE);
				Statement s = db.createStatement();
				ResultSet r = s.executeQuery("SELECT SUM(k.laajuus) FROM Kurssit k, Suoritukset s\r\n"
						+ "WHERE s.kurssi_id=k.id AND s.paivays LIKE '" + syotetty + "%'");) {

			if (r.getString("SUM(k.laajuus)") != null) {
				System.out.println("Opintopisteiden yhteenlaskettu määrä: " + r.getInt("SUM(k.laajuus)"));
			} else {
				System.out.println("Ei löytynyt suorituksia!");
				kysymys(syote);
			}
		} catch (SQLException e) {
			System.out.println(VIRHEDEV);
			System.out.println(VIRHE);
		}
		kysymys(syote);
	}

	/**
	 * Aliohjelma, jossa lasketaan kurssien keskiarvot
	 */
	private static void kurssiKeskiarvo(Scanner syote) throws SQLException {
		System.out.println("Valitse haettavan kurssin nimi: ");
		String vastaus = syote.nextLine();

		try (Connection db = DriverManager.getConnection(KURSSITDATABASE);
				Statement s = db.createStatement();
				ResultSet r = s.executeQuery(
						"SELECT AVG(s.arvosana) FROM kurssit k LEFT JOIN Suoritukset s ON s.kurssi_id = k.id\r\n"
								+ "WHERE nimi = '" + vastaus + "'");) {

			if (r.getString("AVG(s.arvosana)") != null) {
				System.out.println("Kurssien arvosanojen keskiarvo: on " + r.getDouble("AVG(s.arvosana)"));
			} else {
				System.out.println("Kurssia ei löytynyt");
				kysymys(syote);
			}
		} catch (SQLException e) {
			System.out.println(VIRHEDEV);
			System.out.println(VIRHE);
		}
		kysymys(syote);
	}

	/**
	 * Aliohjelma, jossa tulostetaan opiskelijan suoritukset
	 * 
	 * @throws SQLException
	 */
	private static void suorituksetOpiskelija(Scanner syote) throws SQLException {
		System.out.println("Kirjoita opiskelijan nimi jonka suoritukset haluat nähdä: ");
		String vastaus = syote.nextLine();

		try (Connection db = DriverManager.getConnection(KURSSITDATABASE);
				Statement s = db.createStatement();
				ResultSet r = s.executeQuery(
						"SELECT k.nimi, k.laajuus, s.paivays, s.arvosana FROM Kurssit k, Suoritukset s, Opiskelijat o\r\n"
								+ "WHERE s.opiskelija_id = o.id AND k.id = s.kurssi_id AND o.nimi = '" + vastaus
								+ "'\r\n" + "ORDER BY s.paivays ASC\r\n" + "");) {

			if (r.next()) {
				System.out.println(r.getInt("laajuus") + " | " + r.getString("nimi") + " | " + r.getString("paivays")
						+ " | " + r.getInt("arvosana") + "\r\n");
				while (r.next()) {
					System.out.println(r.getInt("laajuus") + " | " + r.getString("nimi") + " | "
							+ r.getString("paivays") + " | " + r.getInt("arvosana") + "\r\n");
				}
			} else {
				System.out.println(VIRHE);
				kysymys(syote);
			}
		} catch (SQLException e) {
			System.out.println(VIRHEDEV);
			e.printStackTrace();
		}
		kysymys(syote);
	}
}