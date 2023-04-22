# Manager
* Apartman yöneticelerinin apartman yönetimini kolaylaştırmak ve apartman sakinlerinin yönetimin yaptıklarından daha rahat haberdar olabilmesi için geliştirilmiştir.
  (Geliştirme aşamasında)
## Kullanılan Yapılar
* Retrofit
* Okhttp
* Glide
* LiveData
* Flow
* Navigation
* Dagger Hilt
* Shared Preferences
* Coroutines
* Data Binding
## Ekranlar Ve İşlevleri
### Karşılama Ekranı
* Shared Preference ile isLogin bilgisi kontrol edilir. True ise doğrudan anasayfaya yanlış ise giriş ekranına yönlendirme yapar.

<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/splashScreen.png" width="200" height="400" />

### Giriş ve Kayıt Ekranı

<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/loginScreen.png" width="200" height="400" />
<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/signupScreen.png" width="200" height="400" />

* Kullanıcının hesabı varsa giriş ekranında gerekli bilgiler ile hesabına giriş yapar
* Daha önce kayıtlı olmayan kullanıcı eğer yönetici ise apartmanına ait isim belirler ve rol seçimini Yönetici yaparak kayıt olduğunda hem kendisi hem de apartmanı için yeni hesap açmış olur.
### Anasayfa


<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/homePage.mp4" controls="controls" style="max-width: 300px;"></video>

* Anasayfada apartman hesabının genel bilgileri yer almaktadır.Kullanıcılar burada yönetici duyurularına,kapıcı duyurularına,apartman sakini isteklerine ve anketlere ulaşabilir.
* Anketler sayfasında dileyen kullanıcı anket sonucunu pdf olarak kaydedip telefonunda saklayabilir.


<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/convertPollToPdf.mp4" controls="controls" style="max-width: 300px;"></video>


### Kullanıcı Profil Sayfası
1- Yönetici Profili

<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/managerProfile.mp4" controls="controls" style="max-width: 200px;"></video>

2- Apartman Sakin Profili


<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/residentProfile.png" width="200" height="400" />


3- Kapıcı Profili


<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/conciergeProfilePage.png" width="200" height="400" />

* Yöneticiler profil sayfasından Kapıcı görevi,gelir gider ,duyuru,anket paylaşımı yapabilir.Aidat ücretini güncelleyebilir ve mevcut verileri silebilir.
* Normal kullanıcılar istek paylaşabilir ve kapıcıya görev verebilir.Aidat ödemesi yaptıklarını belirtebilirler.

<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/residentPayment.mp4" controls="controls" style="max-width: 200px;"/>


* Kapıcılar duyuru paylaşımı yapabilir.

### Kapıcı Sayfası

1- Kapıcı için kapıcı sayfası

<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/conciergePageForConcierge.mp4" controls="controls" style="max-width: 200px;"/>

2- Diğer Kullanıcılar İçin Kapıcı Sayfası


<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/conciergePageForResident.png" width="200" height="400" />


* Kapıcılar bu ekranda mevcut görevleri yapıldı olarak işaretleyebilir
* Diğer kullanıcılar kapıcının yaptığı ve yapacağı görevleri inceleyebilir

### Apartman Sakinleri Ve Sakin Detay Sayfası

<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/residentsAndResidentDetailPage.mp4" controls="controls" style="max-width: 200px;"/>


* Bu ekranda apartmanda bulunan kullanıcılar listelenebilir ve plaka,isim ya da telefon numarasına göre kullanıcılar filtrelenebilir.
* Sohbet ekranına geçiş yapılabilir
* Kullanıcı detay sayfasında kullacının detayları incelenebilir ve kayıtlı numara varsa arama yapılabilir

### Sohbet Ekranı


<img src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/chatPage.png" width="200" height="400" />

* Bu ekranda apartmana kayıtlı kullanıcılar canlı olarak sohbet edebilir.
  * (Görsel hata mevcut düzeltme yapılacak)

### Cüzdan Sayfası


<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/walletPage.mp4" controls="controls" style="max-width: 200px;"/>

* Bu ekranda kapıcının giriş yaptığı gelir ve giderler ve apartmanın sahip olduğu bütçe gözlemlenebilir
* Sakinlerin aidat ödeme durumu incelenebilir
* Dileyen kullanıcı gelir ve giderleri excel dosyası olarak telefonuna kaydedebilir

<video src="https://raw.githubusercontent.com/leventsrr/projectAssets/main/manager/convertFinanceToExcel.mp4" controls="controls" style="max-width: 200px;"/>











