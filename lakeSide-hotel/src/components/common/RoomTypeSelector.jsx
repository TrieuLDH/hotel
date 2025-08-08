import React, { useEffect, useState } from "react";
import { getRoomType } from "../utils/APIFunctions";

const RoomTypeSelector = ({ handleRoomInputChange, newRoom }) => {
    const [roomTypes, setRoomTypes] = useState([""]);
    const [showNewRoomTypeInput, setShowNewRoomTypeInput] = useState(false); // ✅ Sửa ở đây
    const [newRoomType, setNewRoomType] = useState("");

    useEffect(() => {
        getRoomType().then((data) => {
            setRoomTypes(data);
        });
    }, []);

    const handleNewRoomTypeInputChange = (e) => {
        setNewRoomType(e.target.value);
    };

    const handleAddNewRoomType = () => {
        if (newRoomType.trim() !== "") {
            setRoomTypes([...roomTypes, newRoomType]);
            setNewRoomType("");
            setShowNewRoomTypeInput(false);
        }
    };

    return (
        <>
            {roomTypes.length > 0 && (
                <div>
                    <select
                        required
                        name="roomType"
                        className="form-select"
                        onChange={(e) => {
                            if (e.target.value === "Add New") {
                                setShowNewRoomTypeInput(true);
                            } else {
                                setShowNewRoomTypeInput(false); // ✅ Ẩn input nếu chọn lại loại phòng khác
                                handleRoomInputChange(e);
                            }
                        }}
                        value={newRoom.roomType}
                    >
                        <option value="">Select a room type</option>
                        {roomTypes.map((type, index) => (
                            <option key={index} value={type}>
                                {type}
                            </option>
                        ))}
                        <option value="Add New">Add New</option>
                    </select>

                    {showNewRoomTypeInput && (
                        <div className="mt-2">
                            <div className="input-group">
                                <input
                                    className="form-control"
                                    type="text"
                                    placeholder="Enter a new room type"
                                    value={newRoomType}
                                    onChange={handleNewRoomTypeInputChange}
                                />
                                <button
                                    className="btn btn-hotel"
                                    type="button"
                                    onClick={handleAddNewRoomType}
                                >
                                    Add
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </>
    );
};

export default RoomTypeSelector;
