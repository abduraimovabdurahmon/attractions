import "leaflet/dist/leaflet.css";
import { useEffect, useRef, useState } from "react";
import { MapContainer, Marker, Popup, TileLayer, Tooltip } from "react-leaflet";
import { Icon } from "leaflet";

const BASE_URL = "http://localhost:8080";

const categoryes = [
  "theater",
  "park",
  "historical",
  "garden",
  "museum",
  "zoo",
  "religious",
  "transportation",
  "educational",
  "entertainment",
  "market",
  "landmark",
];
const categoryNames = [
  "Teatr",
  "Park",
  "Tarixiy joy",
  "Bog'",
  "Muzey",
  "Hayvonot bog'i",
  "Diniy joylar",
  "Transport",
  "Ta'lim maskani",
  "Ko'ngilochar joy",
  "Bozor",
  "Chiroyli joylar",
];

function App() {
  const position = [41.311081, 69.240562];
  const mapRef = useRef();

  const [accordations, setAccordations] = useState([]);
  const [showAccordations, setShowAccordations] = useState([]);
  const [isPlusButtonOpen, setIsPlusButtonOpen] = useState(false);

  const [isChooseLocationButtonOpen, setIsChooseLocationButtonOpen] =
    useState(false);

  const [newAccordation, setNewAccordation] = useState({
    name: "",
    description: "",
    category: "",
    rating: "",
    website: "",
    latitude: "",
    longitude: "",
  });

  const getAccordations = async () => {
    const responce = await fetch(`${BASE_URL}/attractions`);

    const data = await responce.json();

    console.log(data);

    if (data) {
      setAccordations(data);
      setShowAccordations(data);
    }
  };

  const saveAccordation = async (e) => {
    e.preventDefault();

    try {
      if (
        newAccordation.name === "" ||
        newAccordation.description === "" ||
        newAccordation.category === "" ||
        newAccordation.rating === "" ||
        newAccordation.website === "" ||
        newAccordation.latitude === "" ||
        newAccordation.longitude === ""
      ) {
        alert("Barcha maydonlarni to'ldiring");
        return;
      }

      fetch(`${BASE_URL}/attractions`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newAccordation),
      })
        .then((data) => {
          if (data) {
            alert("Muvaffaqiyatli saqlandi!");
          }
        })
        .then(() => {
          getAccordations();
          setIsPlusButtonOpen(!isPlusButtonOpen);
        });
    } catch (error) {
      alert("Xatolik yuz berdi");
    }
  };

  const deleteAccordation = async (id) => {
    try {
      fetch(`${BASE_URL}/attractions/${id}`, {
        method: "DELETE",
      })
        .then((data) => {
          if (data) {
            alert("Muvaffaqiyatli o'chirildi!");
          }
        })
        .then(() => {
          getAccordations();
        });
    } catch (error) {
      alert("Xatolik yuz berdi");
    }
  };

  useEffect(() => {
    getAccordations();
  }, []);

  useEffect(() => {
    if (isChooseLocationButtonOpen) {
      mapRef.current.on("click", (e) => {
        const { lat, lng } = e.latlng;
        setNewAccordation({
          ...newAccordation,
          latitude: lat,
          longitude: lng,
        });
        setIsChooseLocationButtonOpen(!isChooseLocationButtonOpen);
      });

      // cursor style
      document.querySelector(".leaflet-container").style.cursor = "crosshair";
      
    }
  }, [isChooseLocationButtonOpen]);

  return (
    <div className="rootDiv">
      <div className="absolute">
        {!isPlusButtonOpen && (
          <div className="filter">
            <select
              onChange={(e) => {
                const selectedCategory = e.target.value;
                if (selectedCategory === "all") {
                  setShowAccordations(accordations);
                } else {
                  const filteredAccordations = accordations.filter(
                    (accordation) => {
                      return accordation.category === selectedCategory;
                    }
                  );
                  setShowAccordations(filteredAccordations);
                }
              }}
            >
              <option value="all">Filter</option>
              {categoryes.map((category, index) => {
                return (
                  <option key={index} value={category}>
                    {categoryNames[index]}
                  </option>
                );
              })}
            </select>
          </div>
        )}

        <div className="addAccordation">
          <div
            className="plusButton"
            onClick={() => {
              setIsPlusButtonOpen(!isPlusButtonOpen);
            }}
          >
            {isPlusButtonOpen ? "Yopish" : "Qo'shish"}
          </div>

          {isPlusButtonOpen && (
            <div className="addForm">
              <form action="">
                <input
                  type="text"
                  placeholder="Nomi"
                  onChange={(e) => {
                    setNewAccordation({
                      ...newAccordation,
                      name: e.target.value,
                    });
                  }}
                  value={newAccordation.name}
                />
                <br />
                <input
                  type="text"
                  placeholder="Tavsifi"
                  onChange={(e) => {
                    setNewAccordation({
                      ...newAccordation,
                      description: e.target.value,
                    });
                  }}
                  value={newAccordation.description}
                />
                <br />

                {newAccordation.latitude && newAccordation.longitude ? (
                  <>
                    <p>
                      <b>Lat:</b> {newAccordation.latitude} <br />
                      <b>Long:</b> {newAccordation.longitude} <br />
                      <i style={{fontSize: 10}}>(joylashuvni o'zgartirish uchun xaritani bosing)</i>
                    </p>
                  </>
                ) : (
                  <button
                    className="chooseLocationButton"
                    onClick={(e) => {
                      e.preventDefault();
                      setIsChooseLocationButtonOpen(
                        !isChooseLocationButtonOpen
                      );
                    }}
                  >
                    {isChooseLocationButtonOpen
                      ? "Xaritani ustiga bosing"
                      : "Joylashuvni tanlash"}
                  </button>
                )}

                <br />
                <select
                  name="Kategoriyasi"
                  id="category"
                  onChange={(e) => {
                    setNewAccordation({
                      ...newAccordation,
                      category: e.target.value,
                    });
                  }}
                >
                  {categoryes.map((category, index) => {
                    return (
                      <option key={index} value={category}>
                        {categoryNames[index]}
                      </option>
                    );
                  })}
                </select>
                <br />
                <input
                  type="number"
                  placeholder="Reytingi"
                  onChange={(e) => {
                    setNewAccordation({
                      ...newAccordation,
                      rating: e.target.value,
                    });
                  }}
                  value={newAccordation.rating}
                />
                <br />
                <input
                  type="text"
                  placeholder="Vebsayti"
                  onChange={(e) => {
                    setNewAccordation({
                      ...newAccordation,
                      website: e.target.value,
                    });
                  }}
                  value={newAccordation.website}
                />
                <br />
                <button
                  style={{
                    backgroundColor: "red",
                    color: "white",
                  }}
                  onClick={(e) => {
                    e.preventDefault();
                    setNewAccordation({
                      name: "",
                      description: "",
                      category: "",
                      rating: "",
                      website: "",
                      latitude: "",
                      longitude: "",
                    });
                  }}
                >
                  Tozalash
                </button>

                <button
                  style={{
                    backgroundColor: "green",
                    color: "white",
                  }}
                  onClick={saveAccordation}
                >
                  Saqlash
                </button>
              </form>
            </div>
          )}
        </div>
      </div>

      {/* Map */}
      <MapContainer
        style={{
          height: "100vh",
          width: "100%",
        }}
        center={position}
        zoom={12}
        ref={mapRef}
      >
        {/* add google map tile url  */}
        <TileLayer
          attribution="Google Maps"
          url="https://www.google.cn/maps/vt?lyrs=m@189&gl=cn&x={x}&y={y}&z={z}"
        />
        {showAccordations &&
          showAccordations.map((accordation) => {
            return (
              <Marker
                key={accordation.id}
                position={[accordation.latitude, accordation.longitude]}
              >
                {/*   "educational", "entertainment", "market", "Historical", "landmark"] */}
                <Popup>
                  <h3>{accordation.name}</h3>
                  <p>{accordation.description}</p>
                  {accordation.website && (
                    <a href={accordation.website} target="_blank">
                      {accordation.website}
                    </a>
                  )}
                  <br />
                  {accordation.rating && (
                    <p>
                      <b>Reyting:</b> {accordation.rating}
                    </p>
                  )}

                  <p>
                    <b>Kategoriya:</b> {categoryNames[categoryes.indexOf(accordation.category)]}
                  </p>

                  <button
                    style={{
                      backgroundColor: "red",
                      color: "white",
                      borderRadius: 5,
                      border: "none",
                      padding: 5,
                    }}
                    onClick={() => {deleteAccordation(accordation.id)}}
                  >
                    O'chirish
                  </button>
                </Popup>
                <Tooltip permanent>{accordation.name}</Tooltip>
              </Marker>
            );
          })}
      </MapContainer>
    </div>
  );
}

export default App;
