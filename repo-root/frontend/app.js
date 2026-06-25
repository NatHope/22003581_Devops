
const AIRCRAFT_API = "http://79.72.64.241:8081";
const HANGAR_API = "http://79.72.64.241:8082";

const statusMessage = document.getElementById("status-message");

function showError(message) {
    statusMessage.textContent = message;
    console.error(message);
}

function clearError() {
    statusMessage.textContent = "";
}

// Aircraft

const aircraftTableBody = document.querySelector("#aircraft-table tbody");
const aircraftForm = document.getElementById("aircraft-form");
const aircraftIdField = document.getElementById("aircraft-id");
const aircraftFormTitle = document.getElementById("aircraft-form-title");

async function loadAircraft() {
    try {
        const response = await fetch(`${AIRCRAFT_API}/aircraft`);
        if (!response.ok) {
            throw new Error(`Failed to load aircraft (status ${response.status})`);
        }
        const aircraftList = await response.json();
        renderAircraft(aircraftList);
        clearError();
    } catch (err) {
        showError(`Could not load aircraft: ${err.message}`);
    }
}

function renderAircraft(aircraftList) {
    aircraftTableBody.innerHTML = "";
    aircraftList.forEach(aircraft => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${aircraft.id}</td>
            <td>${aircraft.tailNumber}</td>
            <td>${aircraft.model}</td>
            <td>${aircraft.manufacturer ?? ""}</td>
            <td>${aircraft.capacity ?? ""}</td>
            <td>${aircraft.status}</td>
            <td>
                <button data-action="edit" data-id="${aircraft.id}">Edit</button>
                <button data-action="delete" data-id="${aircraft.id}">Delete</button>
            </td>
        `;
        row.querySelector('[data-action="edit"]').addEventListener("click", () => editAircraft(aircraft));
        row.querySelector('[data-action="delete"]').addEventListener("click", () => deleteAircraft(aircraft.id));
        aircraftTableBody.appendChild(row);
    });
}

function editAircraft(aircraft) {
    aircraftIdField.value = aircraft.id;
    document.getElementById("aircraft-tailNumber").value = aircraft.tailNumber;
    document.getElementById("aircraft-model").value = aircraft.model;
    document.getElementById("aircraft-manufacturer").value = aircraft.manufacturer ?? "";
    document.getElementById("aircraft-capacity").value = aircraft.capacity ?? "";
    document.getElementById("aircraft-status").value = aircraft.status;
    aircraftFormTitle.textContent = `Edit Aircraft #${aircraft.id}`;
}

function resetAircraftForm() {
    aircraftForm.reset();
    aircraftIdField.value = "";
    aircraftFormTitle.textContent = "Add Aircraft";
}

async function deleteAircraft(id) {
    try {
        const response = await fetch(`${AIRCRAFT_API}/aircraft/${id}`, { method: "DELETE" });
        if (!response.ok && response.status !== 204) {
            throw new Error(`Failed to delete aircraft (status ${response.status})`);
        }
        await loadAircraft();
        clearError();
    } catch (err) {
        showError(`Could not delete aircraft: ${err.message}`);
    }
}

aircraftForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const payload = {
        tailNumber: document.getElementById("aircraft-tailNumber").value,
        model: document.getElementById("aircraft-model").value,
        manufacturer: document.getElementById("aircraft-manufacturer").value,
        capacity: Number(document.getElementById("aircraft-capacity").value) || null,
        status: document.getElementById("aircraft-status").value
    };

    const id = aircraftIdField.value;
    const url = id ? `${AIRCRAFT_API}/aircraft/${id}` : `${AIRCRAFT_API}/aircraft`;
    const method = id ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            throw new Error(`Failed to save aircraft (status ${response.status})`);
        }
        resetAircraftForm();
        await loadAircraft();
        clearError();
    } catch (err) {
        showError(`Could not save aircraft: ${err.message}`);
    }
});

document.getElementById("aircraft-cancel").addEventListener("click", resetAircraftForm);


// Hangar Bays

const bayTableBody = document.querySelector("#bay-table tbody");
const bayForm = document.getElementById("bay-form");
const bayIdField = document.getElementById("bay-id");
const bayFormTitle = document.getElementById("bay-form-title");

let cachedBays = [];

async function loadBays() {
    try {
        const response = await fetch(`${HANGAR_API}/bays`);
        if (!response.ok) {
            throw new Error(`Failed to load hangar bays (status ${response.status})`);
        }
        cachedBays = await response.json();
        renderBays(cachedBays);
        clearError();
    } catch (err) {
        showError(`Could not load hangar bays: ${err.message}`);
    }
}

function renderBays(bays) {
    bayTableBody.innerHTML = "";
    bays.forEach(bay => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${bay.id}</td>
            <td>${bay.bayNumber}</td>
            <td>${bay.location ?? ""}</td>
            <td>${bay.capacity ?? ""}</td>
            <td>
                <button data-action="edit" data-id="${bay.id}">Edit</button>
                <button data-action="delete" data-id="${bay.id}">Delete</button>
            </td>
        `;
        row.querySelector('[data-action="edit"]').addEventListener("click", () => editBay(bay));
        row.querySelector('[data-action="delete"]').addEventListener("click", () => deleteBay(bay.id));
        bayTableBody.appendChild(row);
    });
}

function editBay(bay) {
    bayIdField.value = bay.id;
    document.getElementById("bay-bayNumber").value = bay.bayNumber;
    document.getElementById("bay-location").value = bay.location ?? "";
    document.getElementById("bay-capacity").value = bay.capacity ?? "";
    bayFormTitle.textContent = `Edit Bay #${bay.id}`;
}

function resetBayForm() {
    bayForm.reset();
    bayIdField.value = "";
    bayFormTitle.textContent = "Add Hangar Bay";
}

async function deleteBay(id) {
    try {
        const response = await fetch(`${HANGAR_API}/bays/${id}`, { method: "DELETE" });
        if (!response.ok && response.status !== 204) {
            throw new Error(`Failed to delete hangar bay (status ${response.status})`);
        }
        await loadBays();
        clearError();
    } catch (err) {
        showError(`Could not delete hangar bay: ${err.message}`);
    }
}

bayForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const payload = {
        bayNumber: document.getElementById("bay-bayNumber").value,
        location: document.getElementById("bay-location").value,
        capacity: Number(document.getElementById("bay-capacity").value) || null
    };

    const id = bayIdField.value;
    const url = id ? `${HANGAR_API}/bays/${id}` : `${HANGAR_API}/bays`;
    const method = id ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            throw new Error(`Failed to save hangar bay (status ${response.status})`);
        }
        resetBayForm();
        await loadBays();
        await loadBookings();
        clearError();
    } catch (err) {
        showError(`Could not save hangar bay: ${err.message}`);
    }
});

document.getElementById("bay-cancel").addEventListener("click", resetBayForm);


// Bookings

const bookingTableBody = document.querySelector("#booking-table tbody");
const bookingForm = document.getElementById("booking-form");
const bookingIdField = document.getElementById("booking-id");
const bookingFormTitle = document.getElementById("booking-form-title");

async function loadBookings() {
    try {
        const response = await fetch(`${HANGAR_API}/bookings`);
        if (!response.ok) {
            throw new Error(`Failed to load bookings (status ${response.status})`);
        }
        const bookings = await response.json();
        renderBookings(bookings);
        clearError();
    } catch (err) {
        showError(`Could not load bookings: ${err.message}`);
    }
}

function renderBookings(bookings) {
    bookingTableBody.innerHTML = "";
    bookings.forEach(booking => {
        const bayLabel = booking.hangarBay ? booking.hangarBay.bayNumber : booking.bayId;
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${booking.id}</td>
            <td>${booking.aircraftId}</td>
            <td>${bayLabel}</td>
            <td>${booking.startDate}</td>
            <td>${booking.endDate}</td>
            <td>
                <button data-action="edit" data-id="${booking.id}">Edit</button>
                <button data-action="delete" data-id="${booking.id}">Delete</button>
            </td>
        `;
        row.querySelector('[data-action="edit"]').addEventListener("click", () => editBooking(booking));
        row.querySelector('[data-action="delete"]').addEventListener("click", () => deleteBooking(booking.id));
        bookingTableBody.appendChild(row);
    });
}

function editBooking(booking) {
    bookingIdField.value = booking.id;
    document.getElementById("booking-aircraftId").value = booking.aircraftId;
    document.getElementById("booking-bayId").value = booking.hangarBay ? booking.hangarBay.id : booking.bayId;
    document.getElementById("booking-startDate").value = booking.startDate;
    document.getElementById("booking-endDate").value = booking.endDate;
    bookingFormTitle.textContent = `Edit Booking #${booking.id}`;
}

function resetBookingForm() {
    bookingForm.reset();
    bookingIdField.value = "";
    bookingFormTitle.textContent = "Add Booking";
}

async function deleteBooking(id) {
    try {
        const response = await fetch(`${HANGAR_API}/bookings/${id}`, { method: "DELETE" });
        if (!response.ok && response.status !== 204) {
            throw new Error(`Failed to delete booking (status ${response.status})`);
        }
        await loadBookings();
        clearError();
    } catch (err) {
        showError(`Could not delete booking: ${err.message}`);
    }
}

bookingForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const payload = {
        aircraftId: Number(document.getElementById("booking-aircraftId").value),
        bayId: Number(document.getElementById("booking-bayId").value),
        startDate: document.getElementById("booking-startDate").value,
        endDate: document.getElementById("booking-endDate").value
    };

    const id = bookingIdField.value;
    const url = id ? `${HANGAR_API}/bookings/${id}` : `${HANGAR_API}/bookings`;
    const method = id ? "PUT" : "POST";

    try {
        const response = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || `Failed to save booking (status ${response.status})`);
        }
        resetBookingForm();
        await loadBookings();
        clearError();
    } catch (err) {
        showError(`Could not save booking: ${err.message}`);
    }
});

document.getElementById("booking-cancel").addEventListener("click", resetBookingForm);


// Initial load

loadAircraft();
loadBays();
loadBookings();
