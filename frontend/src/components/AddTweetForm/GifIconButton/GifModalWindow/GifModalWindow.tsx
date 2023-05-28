import React, { FC, ReactElement, useEffect, useState } from "react";
import { Dialog, DialogContent, DialogTitle, InputAdornment } from "@material-ui/core";

import { useGlobalStyles } from "../../../../util/globalClasses";
import { SearchIcon } from "../../../../icons";
import { MainSearchTextField } from "../../../SearchTextField/MainSearchTextField";
import CloseButton from "../../../CloseButton/CloseButton";
import { useInputText } from "../../../../hook/useInputText";
import { useDebounce } from "../../../../hook/useDebounce";
import { axios } from "../../../../core/axios";
import GifTopics from "./GifTopics/GifTopics";
import GifList from "./GifList/GifList";
import { GIPHY_API_URL } from "../../../../constants/url-constants";
import Spinner from "../../../Spinner/Spinner";

interface GifModalWindowProps {
    visible: boolean;
    onClose: () => void;
}

export interface GiphyDataProps {
    id: string;
    title: string;
    images: {
        downsized: { url: string }
        downsized_still: { url: string }
    };
}

const GifModalWindow: FC<GifModalWindowProps> = ({ visible, onClose }): ReactElement | null => {
    const globalClasses = useGlobalStyles({});
    const [gifs, setGifs] = useState<GiphyDataProps[]>([]);
    const [isGifsLoading, setIsGifsLoading] = useState(false);
    const { text, setText, handleChangeText } = useInputText();
    const textToSearch = useDebounce(text, 300);

    useEffect(() => {
        if (textToSearch) {
            searchGif(text);
        }
    }, [textToSearch]);

    const searchGif = async (text: string): Promise<void> => {
        setGifs([]);
        setIsGifsLoading(true);
        await axios.get(GIPHY_API_URL + text)
            .then((response) => {
                setIsGifsLoading(false);
                setGifs(response.data.data);
            })
            .catch((error) => {
                setIsGifsLoading(false);
                console.log(error);
            });
    };

    const onClickGifTopic = (topic: string): void => {
        setText(topic);
        setGifs([]);
        setIsGifsLoading(true);
    };

    const onCloseModalWindow = (): void => {
        setGifs([]);
        onClose();
    };

    if (!visible) {
        return null;
    }

    return (
        <Dialog open={visible} onClose={onCloseModalWindow}>
            <DialogTitle>
                <CloseButton onClose={onCloseModalWindow} />
                <MainSearchTextField
                    variant="outlined"
                    placeholder="Search for GIFs"
                    onChange={handleChangeText}
                    value={text}
                    InputProps={{
                        startAdornment: (
                            <InputAdornment position="start">
                                {SearchIcon}
                            </InputAdornment>
                        )
                    }}
                    marginTop={0}
                    width={520}
                />
            </DialogTitle>
            <DialogContent className={globalClasses.dialogContent}>
                {(isGifsLoading && text !== "") ? (
                    <Spinner />
                ) : (
                    (!isGifsLoading && text === "") ? (
                        <GifTopics onClickGifTopic={onClickGifTopic} />
                    ) : (
                        <GifList gifs={gifs} />
                    )
                )}
            </DialogContent>
        </Dialog>
    );
};

export default GifModalWindow;
