import React from "react";

interface IProps {
    value: number;
    max: number;
}

const ProgressBar: React.FC<IProps> = ({ value, max }) => {
    return <progress className="mx_ProgressBar" max={max} value={value} />;
};

export default ProgressBar;
