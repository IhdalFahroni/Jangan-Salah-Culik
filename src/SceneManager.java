
import java.util.ArrayList;
import java.util.List;

public class SceneManager {
    private Scene currentScene;
    private List<Scene> sceneHistory;
    private UIManager uiManager;

    public SceneManager(UIManager uiManager) {
        this.sceneHistory = new ArrayList<>();
        this.uiManager = uiManager;
    }

    public void loadScene(int sceneId) {
        Scene scene = new Scene();
        scene.setSceneId(sceneId);
        List<Character> characters = new ArrayList<>();
        List<Choice> choices = new ArrayList<>();
        switch (sceneId) {
            case 1:
                scene.setSceneTitle("Markas Menteng 31");
                scene.setSceneDescription(
                        "Malam hari di markas pemuda Menteng 31. Radio menyala, asap rokok tebal, Chaerul Saleh mendesak aksi cepat."
                                +
                                "\nChaerul memukul meja, Wikana menatap Sutan Sjahrir, dan kabar menyerahnya Jepang mengguncang semua orang.");
                scene.setBackgroundImage("menteng31.jpg");
                scene.setBackgroundMusic("radio_static.ogg");
                Character chaerul = new Character();
                chaerul.setCharacterName("Chaerul Saleh");
                chaerul.setCharacterRole("Pemuda Revolusioner");
                characters.add(chaerul);
                Character wikana = new Character();
                wikana.setCharacterName("Wikana");
                wikana.setCharacterRole("Penggerak Pemuda");
                characters.add(wikana);
                Character sjahrir = new Character();
                sjahrir.setCharacterName("Sutan Sjahrir");
                sjahrir.setCharacterRole("Tokoh Politik");
                characters.add(sjahrir);
                Choice c1a = new Choice();
                c1a.setChoiceId(1);
                c1a.setChoiceText("A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!");
                c1a.setNextSceneId(2);
                c1a.getRelationshipImpact().put("PEMUDA", 20);
                c1a.getRelationshipImpact().put("TRUST", 10);
                choices.add(c1a);
                Choice c1b = new Choice();
                c1b.setChoiceId(2);
                c1b.setChoiceText("B: Mending tunggu konfirmasi resmi dulu.");
                c1b.setNextSceneId(2);
                c1b.getRelationshipImpact().put("PEMUDA", -15);
                c1b.getRelationshipImpact().put("TRUST", -10);
                choices.add(c1b);
                break;
            case 2:
                scene.setSceneTitle("Rumah Soekarno");
                scene.setSceneDescription(
                        "Pukul 03.00 pagi di Pegangsaan Timur. Soekarno terserang malaria, Fatmawati membuka pintu dengan cemas."
                                +
                                "\nKetukan keras di pintu membuat suasana semakin genting.");
                scene.setBackgroundImage("rumah_soekarno.jpg");
                scene.setBackgroundMusic("night_ambience.ogg");
                Character soekarno = new Character();
                soekarno.setCharacterName("Soekarno");
                soekarno.setCharacterRole("Pemimpin Bangsa");
                characters.add(soekarno);
                Character hatta = new Character();
                hatta.setCharacterName("Mohammad Hatta");
                hatta.setCharacterRole("Pemimpin Bangsa");
                characters.add(hatta);
                Character fatma = new Character();
                fatma.setCharacterName("Fatmawati");
                fatma.setCharacterRole("Pendamping Soekarno");
                characters.add(fatma);
                Choice c2a = new Choice();
                c2a.setChoiceId(1);
                c2a.setChoiceText("A: Pendekatan diplomatis untuk membujuk Bung Karno.");
                c2a.setNextSceneId(3);
                c2a.getRelationshipImpact().put("SOEKARNO", 20);
                c2a.getRelationshipImpact().put("HATTA", 15);
                c2a.getRelationshipImpact().put("TRUST", 15);
                choices.add(c2a);
                Choice c2b = new Choice();
                c2b.setChoiceId(2);
                c2b.setChoiceText("B: Pendekatan keras dengan ancaman.");
                c2b.setNextSceneId(3);
                c2b.getRelationshipImpact().put("SOEKARNO", -30);
                c2b.getRelationshipImpact().put("HATTA", -20);
                c2b.getRelationshipImpact().put("TRUST", -20);
                choices.add(c2b);
                break;
            case 3:
                scene.setSceneTitle("Perjalanan ke Rengasdengklok");
                scene.setSceneDescription(
                        "Truk tua melaju di jalan gelap menuju Karawang. Kabut pagi dan pos Kempeitai menambah ketegangan."
                                +
                                "\nSoekarno menggigil demam, Hatta khawatir, dan teriakan 'Tomare!' menggema.");
                scene.setBackgroundImage("perjalanan_truk.jpg");
                scene.setBackgroundMusic("engine_rumble.ogg");
                Character sopir = new Character();
                sopir.setCharacterName("Supir PETA");
                sopir.setCharacterRole("Pengemudi Rombongan");
                characters.add(sopir);
                Character pengawal = new Character();
                pengawal.setCharacterName("Sukarni");
                pengawal.setCharacterRole("Pengawal Pemuda");
                characters.add(pengawal);
                Character penjaga = new Character();
                penjaga.setCharacterName("Penjaga Kempeitai");
                penjaga.setCharacterRole("Penutup Jalan");
                characters.add(penjaga);
                Choice c3a = new Choice();
                c3a.setChoiceId(1);
                c3a.setChoiceText("A: Terobos barikade!");
                c3a.setNextSceneId(4);
                c3a.getRelationshipImpact().put("PEMUDA", 15);
                c3a.getRelationshipImpact().put("SOEKARNO", -10);
                c3a.getRelationshipImpact().put("TRUST", 5);
                choices.add(c3a);
                Choice c3b = new Choice();
                c3b.setChoiceId(2);
                c3b.setChoiceText("B: Menyamar sebagai tentara PETA.");
                c3b.setNextSceneId(4);
                c3b.getRelationshipImpact().put("SOEKARNO", 10);
                c3b.getRelationshipImpact().put("HATTA", 15);
                c3b.getRelationshipImpact().put("TRUST", 20);
                choices.add(c3b);
                break;
            case 4:
                scene.setSceneTitle("Rumah Djiaw Kie Siong");
                scene.setSceneDescription(
                        "Pagi buta di rumah petani sederhana. Djiaw terkejut menerima tamu penting, Soekarno masih lemah, Hatta ragu.");
                scene.setBackgroundImage("rumah_djiaw.jpg");
                scene.setBackgroundMusic("morning_rooster.ogg");
                Character djiaw = new Character();
                djiaw.setCharacterName("Djiaw Kie Siong");
                djiaw.setCharacterRole("Tuan Rumah");
                characters.add(djiaw);
                Character pengusaha = new Character();
                pengusaha.setCharacterName("Wangsa Mulia");
                pengusaha.setCharacterRole("Tetangga Pendukung");
                characters.add(pengusaha);
                Character pengawas = new Character();
                pengawas.setCharacterName("Shodanco Singgih");
                pengawas.setCharacterRole("Pengawas Pemuda");
                characters.add(pengawas);
                Choice c4a = new Choice();
                c4a.setChoiceId(1);
                c4a.setChoiceText("A: Jelaskan strategi persembunyian.");
                c4a.setNextSceneId(5);
                c4a.getRelationshipImpact().put("HATTA", 20);
                c4a.getRelationshipImpact().put("SOEKARNO", 10);
                c4a.getRelationshipImpact().put("TRUST", 15);
                choices.add(c4a);
                Choice c4b = new Choice();
                c4b.setChoiceId(2);
                c4b.setChoiceText("B: Mengaku tidak punya rencana lain.");
                c4b.setNextSceneId(5);
                c4b.getRelationshipImpact().put("HATTA", -15);
                c4b.getRelationshipImpact().put("TRUST", -10);
                choices.add(c4b);
                break;
            case 5:
                scene.setSceneTitle("Debat Kemerdekaan");
                scene.setSceneDescription(
                        "Ruang tamu rumah Djiaw dipenuhi debat sengit. Soekarno dan Hatta masih ragu, pemuda mendesak."
                                +
                                "\nChaerul Saleh menuntut proklamasi segera.");
                scene.setBackgroundImage("debat_kemerdekaan.jpg");
                scene.setBackgroundMusic("heated_debate.ogg");
                Character chaerulDebat = new Character();
                chaerulDebat.setCharacterName("Chaerul Saleh");
                chaerulDebat.setCharacterRole("Pemimpin Pemuda");
                characters.add(chaerulDebat);
                Character sukad = new Character();
                sukad.setCharacterName("Sukarni");
                sukad.setCharacterRole("Pemicu Perdebatan");
                characters.add(sukad);
                Character latief = new Character();
                latief.setCharacterName("Latief Hendraningrat");
                latief.setCharacterRole("Pengaman Rencana");
                characters.add(latief);
                Choice c5a = new Choice();
                c5a.setChoiceId(1);
                c5a.setChoiceText("A: Gunakan fakta militer untuk meyakinkan.");
                c5a.setNextSceneId(6);
                c5a.getRelationshipImpact().put("SOEKARNO", 15);
                c5a.getRelationshipImpact().put("TRUST", 10);
                choices.add(c5a);
                Choice c5b = new Choice();
                c5b.setChoiceId(2);
                c5b.setChoiceText("B: Gunakan ancaman emosional.");
                c5b.setNextSceneId(6);
                c5b.getRelationshipImpact().put("PEMUDA", 25);
                c5b.getRelationshipImpact().put("SOEKARNO", -5);
                c5b.getRelationshipImpact().put("TRUST", 15);
                choices.add(c5b);
                break;
            case 6:
                scene.setSceneTitle("Kedatangan Ahmad Soebardjo");
                scene.setSceneDescription(
                        "Sore hari di Rengasdengklok. Ahmad Soebardjo tiba dengan kabar bahwa proklamasi bisa dilakukan besok."
                                +
                                "\nSoekarno lega, pemuda masih curiga.");
                scene.setBackgroundImage("kedatangan_soebardjo.jpg");
                scene.setBackgroundMusic("relief_theme.ogg");
                Character soebardjo = new Character();
                soebardjo.setCharacterName("Ahmad Soebardjo");
                soebardjo.setCharacterRole("Diplomat Penghubung");
                characters.add(soebardjo);
                Character soekarno6 = new Character();
                soekarno6.setCharacterName("Soekarno");
                soekarno6.setCharacterRole("Tokoh Sentral");
                characters.add(soekarno6);
                Character hatta6 = new Character();
                hatta6.setCharacterName("Mohammad Hatta");
                hatta6.setCharacterRole("Negosiator Rasional");
                characters.add(hatta6);
                Choice c6a = new Choice();
                c6a.setChoiceId(1);
                c6a.setChoiceText("A: Percaya pada Soebardjo.");
                c6a.setNextSceneId(7);
                c6a.getRelationshipImpact().put("SOEKARNO", 20);
                c6a.getRelationshipImpact().put("HATTA", 20);
                c6a.getRelationshipImpact().put("TRUST", 25);
                choices.add(c6a);
                Choice c6b = new Choice();
                c6b.setChoiceId(2);
                c6b.setChoiceText("B: Tidak percaya, tahan Soebardjo.");
                c6b.setNextSceneId(7);
                c6b.getRelationshipImpact().put("PEMUDA", 15);
                c6b.getRelationshipImpact().put("SOEKARNO", -20);
                c6b.getRelationshipImpact().put("TRUST", -30);
                choices.add(c6b);
                break;
            case 7:
                scene.setSceneTitle("Konfrontasi dengan Jepang");
                scene.setSceneDescription(
                        "Rumah Laksamana Maeda dipenuhi ketegangan. Nishimura melarang proklamasi, Maeda menengahi." +
                                "\nSoekarno-Hatta harus memutuskan sikap.");
                scene.setBackgroundImage("konfrontasi_jepang.jpg");
                scene.setBackgroundMusic("tension_strings.ogg");
                Character maeda = new Character();
                maeda.setCharacterName("Laksamana Maeda");
                maeda.setCharacterRole("Pelindung Diam-Diam");
                characters.add(maeda);
                Character nishimura = new Character();
                nishimura.setCharacterName("Jenderal Nishimura");
                nishimura.setCharacterRole("Utusan Jepang");
                characters.add(nishimura);
                Character soekarno7 = new Character();
                soekarno7.setCharacterName("Soekarno");
                soekarno7.setCharacterRole("Tokoh Utama");
                characters.add(soekarno7);
                Character hatta7 = new Character();
                hatta7.setCharacterName("Mohammad Hatta");
                hatta7.setCharacterRole("Penengah Diplomatis");
                characters.add(hatta7);
                Choice c7a = new Choice();
                c7a.setChoiceId(1);
                c7a.setChoiceText("A: Konfrontasi langsung dengan Nishimura.");
                c7a.setNextSceneId(8);
                c7a.getRelationshipImpact().put("PEMUDA", 30);
                c7a.getRelationshipImpact().put("SOEKARNO", -40);
                c7a.getRelationshipImpact().put("TRUST", -50);
                choices.add(c7a);
                Choice c7b = new Choice();
                c7b.setChoiceId(2);
                c7b.setChoiceText("B: Bermain diam-diam, susun naskah secara rahasia.");
                c7b.setNextSceneId(8);
                c7b.getRelationshipImpact().put("SOEKARNO", 25);
                c7b.getRelationshipImpact().put("HATTA", 20);
                c7b.getRelationshipImpact().put("TRUST", 30);
                choices.add(c7b);
                break;
            case 8:
                scene.setSceneTitle("Perumusan Naskah");
                scene.setSceneDescription(
                        "Dini hari di ruang belakang rumah Maeda. Soekarno, Hatta, dan Sayuti Melik merumuskan naskah proklamasi.");
                scene.setBackgroundImage("perumusan_naskah.jpg");
                scene.setBackgroundMusic("typewriter_loop.ogg");
                Character soekarno8 = new Character();
                soekarno8.setCharacterName("Soekarno");
                soekarno8.setCharacterRole("Penyusun Utama");
                characters.add(soekarno8);
                Character hatta8 = new Character();
                hatta8.setCharacterName("Mohammad Hatta");
                hatta8.setCharacterRole("Pengarah Kalimat");
                characters.add(hatta8);
                Character sayuti = new Character();
                sayuti.setCharacterName("Sayuti Melik");
                sayuti.setCharacterRole("Pengetik Proklamasi");
                characters.add(sayuti);
                Choice c8a = new Choice();
                c8a.setChoiceId(1);
                c8a.setChoiceText("A: Kalimat diplomatis yang aman.");
                c8a.setNextSceneId(9);
                c8a.getRelationshipImpact().put("SOEKARNO", 15);
                c8a.getRelationshipImpact().put("HATTA", 15);
                c8a.getRelationshipImpact().put("TRUST", 20);
                choices.add(c8a);
                Choice c8b = new Choice();
                c8b.setChoiceId(2);
                c8b.setChoiceText("B: Kalimat revolusioner yang provokatif.");
                c8b.setNextSceneId(9);
                c8b.getRelationshipImpact().put("PEMUDA", 20);
                c8b.getRelationshipImpact().put("TRUST", -15);
                choices.add(c8b);
                break;
            case 9:
                scene.setSceneTitle("Momen Bersejarah");
                scene.setSceneDescription(
                        "Pagi 17 Agustus 1945. Massa berkumpul, tentara Jepang siaga. Pilih lokasi pembacaan proklamasi.");
                scene.setBackgroundImage("proklamasi_pagi.jpg");
                scene.setBackgroundMusic("anthem_build.ogg");
                Character soekarno9 = new Character();
                soekarno9.setCharacterName("Soekarno");
                soekarno9.setCharacterRole("Pembaca Proklamasi");
                characters.add(soekarno9);
                Character hatta9 = new Character();
                hatta9.setCharacterName("Mohammad Hatta");
                hatta9.setCharacterRole("Pendamping Pembacaan");
                characters.add(hatta9);
                Character fatma9 = new Character();
                fatma9.setCharacterName("Fatmawati");
                fatma9.setCharacterRole("Penjahit Merah Putih");
                characters.add(fatma9);
                Character latief9 = new Character();
                latief9.setCharacterName("Latief Hendraningrat");
                latief9.setCharacterRole("Pengibar Bendera");
                characters.add(latief9);
                Choice c9a = new Choice();
                c9a.setChoiceId(1);
                c9a.setChoiceText("A: Lapangan Ikada - heroik tapi berbahaya.");
                c9a.setNextSceneId(9);
                c9a.getRelationshipImpact().put("PEMUDA", 40);
                c9a.getRelationshipImpact().put("TRUST", -60);
                choices.add(c9a);
                Choice c9b = new Choice();
                c9b.setChoiceId(2);
                c9b.setChoiceText("B: Rumah Soekarno - aman dan terkontrol.");
                c9b.setNextSceneId(9);
                c9b.getRelationshipImpact().put("SOEKARNO", 30);
                c9b.getRelationshipImpact().put("HATTA", 25);
                c9b.getRelationshipImpact().put("TRUST", 40);
                choices.add(c9b);
                break;
            default:
                scene.setSceneTitle("Scene " + sceneId);
                scene.setSceneDescription("Placeholder description for scene " + sceneId);
        }
        scene.setCharacters(characters);
        scene.setChoices(choices);
        setCurrentScene(scene);
        sceneHistory.add(scene);
        if (uiManager != null) {
            uiManager.showScreen("SCENE_" + sceneId);
            uiManager.updateUI();
        }
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void goToNextScene() {
        if (currentScene == null) {
            loadScene(1);
            return;
        }
        int nextSceneId = currentScene.getSceneId() + 1;
        loadScene(nextSceneId);
    }

    public List<Choice> getAvailableChoices() {
        if (currentScene != null && currentScene.getChoices() != null) {
            return currentScene.getChoices();
        }
        return new ArrayList<>();
    }

    // Getters and Setters
    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public List<Scene> getSceneHistory() {
        return sceneHistory;
    }

    public void setSceneHistory(List<Scene> sceneHistory) {
        this.sceneHistory = sceneHistory;
    }
}
