import React, { useEffect, useState } from "react";
import { getAllRooms } from "../utils/APIFunctions";
import RoomFilter from "../common/RoomFilter";
import RoomPaginator from "../common/RoomPaginator";
import { Row, Col } from "react-bootstrap";





const ExistingRooms = () => {
    const[rooms, setRooms] = useState([])
    const[currentPage, setCurrentPage] = useState(1)
    const[roomsPerPage] = useState(8)
    const[isLoading, setIsLoading] = useState(false)
    const[filteredRooms, setFilteredRooms] = useState([{id: "", roomType: "", roomPrice: ""}])
    // eslint-disable-next-line no-unused-vars
    const[selectedRoomType, setSelectedRoomType] = useState("")
    // eslint-disable-next-line no-unused-vars
    const[successMessage, setSuccessMessage] = useState("")
    // eslint-disable-next-line no-unused-vars
    const[errorMessage, setErrorMessage] = useState("")


    useEffect(() => {
        fetchRooms()

    }, [])


const fetchRooms = async() => {
    setIsLoading(true)
    try {
        const result = await getAllRooms()
        setRooms(result)
        setIsLoading(false)
    } catch (error) {
        setErrorMessage(error.message)
        
    }
}


useEffect(() => {
    if(selectedRoomType === ""){
        setFilteredRooms(rooms)
    } else {
        const filtered = rooms.filter((room) => room.roomType === selectedRoomType)
        setFilteredRooms(filtered)
    }
    setCurrentPage(1)
}, [rooms, selectedRoomType])


const handlePaginationClick = (pageNumber) => {
    setCurrentPage(pageNumber)
}


const calculateTotalPages = (filteredRooms, roomsPerPage, rooms) => {
    const totalRooms = filteredRooms.length > 0 ? filteredRooms.length : rooms.length
    return Math.ceil(totalRooms / roomsPerPage)
}

const indexOfLastRoom = currentPage * roomsPerPage
const indexOfFirstRoom = indexOfLastRoom - roomsPerPage
const currentRooms = filteredRooms.slice(indexOfFirstRoom, indexOfLastRoom)

return (
    <>
    {isLoading ? (
        <p>Cho Loading nhe Trieu</p>
    ): (
        <>
        <section className="container">
        <div className="d-flex justify-content-center mb-3 mt-5">
            <h2 className="read-the-docs">Existing Rooms</h2>
        </div>

        <Row className="mb-3">
            <Col md={6} className="mb-3 mb-md-0">
            <RoomFilter data={rooms} setFilteredData={setFilteredRooms} />
            </Col>
        </Row>

        <table className="table table-bordered table-hover">
            <thead>
            <tr className="text-center">
                <th>ID</th>
                <th>Room Type</th>
                <th>Room Price</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            {currentRooms.map((room) => (
                <tr key={room.id} className="text-center">
                 <td>{room.id}</td>   
                <td>{room.roomType}</td>
                <td>{room.roomPrice}</td>
                <td>
                    <button className="btn btn-outline-primary">View / Edit</button>
                    <button className="btn btn-outline-danger ms-2">Delete</button>
                </td>
                </tr>
            ))}
            </tbody>
        </table>

        <RoomPaginator
            currentPage={currentPage}
            totalPages={calculateTotalPages(filteredRooms, roomsPerPage, rooms)}
            onPageChange={handlePaginationClick}
        />
        </section>


        </>
    )}
    </>
)
}


export default ExistingRooms